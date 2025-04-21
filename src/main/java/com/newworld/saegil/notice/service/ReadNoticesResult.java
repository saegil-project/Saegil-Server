package com.newworld.saegil.notice.service;

import java.util.List;

public record ReadNoticesResult(
        List<NoticeDto> notices,
        Long lastResultId,
        boolean hasNext
) {
}
