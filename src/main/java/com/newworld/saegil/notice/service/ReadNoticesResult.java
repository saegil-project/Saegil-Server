package com.newworld.saegil.notice.service;

import java.time.LocalDate;
import java.util.List;

public record ReadNoticesResult(
        List<NoticeDto> notices,
        LocalDate lastResultDate,
        Long lastResultId,
        boolean hasNext
) {
}
