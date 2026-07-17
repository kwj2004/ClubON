package com.eulji.clubon.domain.club.util;

import com.eulji.clubon.domain.club.dto.RecruitmentStatusInfo;
import com.eulji.clubon.domain.club.entity.ClubStatus;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RecruitmentStatusResolver {

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2,4})\\s*[.]\\s*(\\d{1,2})\\s*[.]\\s*(\\d{1,2})");

    private RecruitmentStatusResolver() {
    }

    public static RecruitmentStatusInfo resolve(ClubStatus clubStatus, String recruitPeriod) {
        if (clubStatus == ClubStatus.CLOSED) {
            return new RecruitmentStatusInfo("CLOSED", "모집 종료", false);
        }

        if (recruitPeriod == null || recruitPeriod.isBlank()) {
            return new RecruitmentStatusInfo("ALWAYS_OPEN", "상시 모집", true);
        }

        LocalDate endDate = findLastDate(recruitPeriod);
        if (endDate == null) {
            return new RecruitmentStatusInfo("UNKNOWN", "모집 정보 없음", false);
        }

        boolean isRecruiting = !LocalDate.now().isAfter(endDate);
        if (isRecruiting) {
            return new RecruitmentStatusInfo("RECRUITING", "모집중", true);
        }

        return new RecruitmentStatusInfo("CLOSED", "모집 종료", false);
    }

    private static LocalDate findLastDate(String recruitPeriod) {
        if (recruitPeriod == null || recruitPeriod.isBlank()) {
            return null;
        }

        Matcher matcher = DATE_PATTERN.matcher(recruitPeriod);
        LocalDate lastDate = null;

        while (matcher.find()) {
            lastDate = toLocalDate(
                matcher.group(1),
                matcher.group(2),
                matcher.group(3)
            );
        }

        return lastDate;
    }

    private static LocalDate toLocalDate(String yearValue, String monthValue, String dayValue) {
        int year = Integer.parseInt(yearValue);
        int month = Integer.parseInt(monthValue);
        int day = Integer.parseInt(dayValue);

        if (year < 100) {
            year += 2000;
        }

        return LocalDate.of(year, month, day);
    }
}
