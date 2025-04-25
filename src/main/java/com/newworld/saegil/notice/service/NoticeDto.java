package com.newworld.saegil.notice.service;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeType;

import java.time.LocalDate;

public record NoticeDto(

        Long id,
        String title,
        String content,
        int sourceId,
        NoticeType noticeType,
        LocalDate date,
        String webLink
) {

    public static NoticeDto from(Notice notice) {
        return new NoticeDto(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getSourceId(),
                notice.getNoticeType(),
                notice.getDate(),
                notice.getWebLink()
        );
    }
}
