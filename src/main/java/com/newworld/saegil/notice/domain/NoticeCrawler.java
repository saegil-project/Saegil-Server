package com.newworld.saegil.notice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface NoticeCrawler {

    Set<NoticeType> getSupportingNoticeType();

    List<Notice> crawl(final NoticeType noticeType, final LocalDate lastDate);
}
