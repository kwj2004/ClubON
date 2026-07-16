package com.eulji.clubon.domain.club.service;

import com.eulji.clubon.domain.club.dto.RecordImageUploadRequest;
import com.eulji.clubon.domain.club.dto.RecordImageUploadResponse;
import com.eulji.clubon.domain.club.entity.ClubMemberRole;
import com.eulji.clubon.domain.club.repository.ClubMembershipRepository;
import com.eulji.clubon.domain.club.repository.ClubRepository;
import com.eulji.clubon.global.error.ClubNotFoundException;
import com.eulji.clubon.global.error.StorageOperationFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.core.exception.SdkException;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubRecordImageService {

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "image/jpeg", "image/png", "image/webp"
    );

    private final S3Presigner s3Presigner;
    private final ClubRepository clubRepository;
    private final ClubMembershipRepository clubMembershipRepository;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.upload-url-expiration}")
    private long uploadUrlExpiration;

    @Value("${aws.s3.download-url-expiration}")
    private long downloadUrlExpiration;

    public RecordImageUploadResponse createUploadUrl(Long clubId, String email, RecordImageUploadRequest request) {
        validateClubAdmin(clubId, email);
        validateFile(request);
        validateBucket();

        String objectKey = "club-records/%d/%s.%s".formatted(
            clubId,
            UUID.randomUUID(),
            extension(request.contentType())
        );

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(objectKey)
            .contentType(request.contentType().toLowerCase(Locale.ROOT))
            .build();

        String uploadUrl;
        try {
            uploadUrl = s3Presigner.presignPutObject(PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(uploadUrlExpiration))
                    .putObjectRequest(putObjectRequest)
                    .build())
                .url()
                .toExternalForm();
        } catch (SdkException e) {
            throw new StorageOperationFailedException(e);
        }

        return new RecordImageUploadResponse(uploadUrl, objectKey, uploadUrlExpiration);
    }

    public List<String> createDownloadUrls(List<String> objectKeys) {
        if (objectKeys == null) return List.of();
        return objectKeys.stream().map(this::createDownloadUrl).toList();
    }

    public String createDownloadUrl(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) return null;
        if (objectKey.startsWith("http://") || objectKey.startsWith("https://")) return objectKey;
        validateBucket();

        GetObjectRequest request = GetObjectRequest.builder().bucket(bucket).key(objectKey).build();
        try {
            return s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(downloadUrlExpiration))
                    .getObjectRequest(request)
                    .build())
                .url()
                .toExternalForm();
        } catch (SdkException e) {
            throw new StorageOperationFailedException(e);
        }
    }

    public void validateRecordImageKeys(Long clubId, List<String> objectKeys) {
        if (objectKeys == null) return;
        String prefix = "club-records/" + clubId + "/";
        boolean invalid = objectKeys.stream().anyMatch(key -> key == null || !key.startsWith(prefix));
        if (invalid) throw new IllegalArgumentException("해당 동아리에 업로드된 활동 기록 이미지만 사용할 수 있습니다.");
    }

    private void validateClubAdmin(Long clubId, String email) {
        if (!clubRepository.existsById(clubId)) throw new ClubNotFoundException();
        if (!clubMembershipRepository.existsByClub_IdAndMember_EmailAndRole(clubId, email, ClubMemberRole.ADMIN))
            throw new AccessDeniedException("해당 동아리의 이미지를 업로드할 권한이 없습니다.");
    }

    private void validateFile(RecordImageUploadRequest request) {
        String contentType = request.contentType().toLowerCase(Locale.ROOT);
        if (!ALLOWED_CONTENT_TYPES.contains(contentType))
            throw new IllegalArgumentException("JPG, PNG, WebP 이미지만 업로드할 수 있습니다.");
        if (request.fileSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("이미지는 파일당 최대 10MB까지 업로드할 수 있습니다.");
    }

    private void validateBucket() {
        if (bucket == null || bucket.isBlank())
            throw new IllegalStateException("S3 버킷 설정이 필요합니다.");
    }

    private String extension(String contentType) {
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다.");
        };
    }
}
