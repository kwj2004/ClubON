package com.eulji.clubon.domain.club.util;

import com.eulji.clubon.domain.club.dto.RecruitmentStatusInfo;
import com.eulji.clubon.domain.club.entity.ClubStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class RecruitmentStatusResolverTest {

    @Test
    void openClubWithoutPeriodIsAlwaysOpen() {
        RecruitmentStatusInfo result = RecruitmentStatusResolver.resolve(ClubStatus.OPEN, "");

        assertThat(result.status()).isEqualTo("ALWAYS_OPEN");
        assertThat(result.label()).isEqualTo("상시 모집");
        assertThat(result.isRecruiting()).isTrue();
    }

    @Test
    void closedClubWithoutPeriodIsClosed() {
        RecruitmentStatusInfo result = RecruitmentStatusResolver.resolve(ClubStatus.CLOSED, null);

        assertThat(result.status()).isEqualTo("CLOSED");
        assertThat(result.isRecruiting()).isFalse();
    }

    @Test
    void openClubWithFutureEndDateIsRecruiting() {
        String endDate = LocalDate.now().plusDays(30)
                .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        RecruitmentStatusInfo result = RecruitmentStatusResolver.resolve(
                ClubStatus.OPEN,
                "2026.01.01 ~ " + endDate
        );

        assertThat(result.status()).isEqualTo("RECRUITING");
        assertThat(result.isRecruiting()).isTrue();
    }
}
