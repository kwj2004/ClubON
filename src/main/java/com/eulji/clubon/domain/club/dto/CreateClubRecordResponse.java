package com.eulji.clubon.domain.club.dto;

import com.eulji.clubon.domain.club.entity.ClubRecord;

public record CreateClubRecordResponse(
    Long recordId
) {

    public static CreateClubRecordResponse from(ClubRecord record) {
        return new CreateClubRecordResponse(record.getId());
    }
}
