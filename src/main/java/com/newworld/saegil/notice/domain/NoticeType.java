package com.newworld.saegil.notice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NoticeType {
    HANA_RECRUIT_NOTICE(
            1,
            "남북하나재단",
            "모집공고",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=51",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=51&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=51&act=detail&idx="
    ),
    HANA_GENERAL_ANNOUNCEMENT(
            1,
            "남북하나재단",
            "일반공지",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=52",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=52&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=52&act=detail&idx="
    ),
    HANA_TENDER_NOTICE(
            1,
            "남북하나재단",
            "입찰공고",
            "https://www.koreahana.or.kr/home/kor/notificationField/tenderNotice/index.do?menuPos=53",
            "https://www.koreahana.or.kr/home/kor/notificationField/tenderNotice/index.do?menuPos=53&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/notificationField/tenderNotice/edit.do?menuPos=53&searchValue1=0&idx="
    ),
    HANA_JOB_OPENING(
            1,
            "남북하나재단",
            "채용공고",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=54",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=54&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=54&act=detail&idx="
    ),
    HANA_HOUSING_NOTICE(
            1,
            "남북하나재단",
            "주택공지",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=55",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=55&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=55&act=detail&idx="
    ),
    HANA_RELATED_AGENCY_NOTICE(
            1,
            "남북하나재단",
            "유관기관공지",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=56",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=56&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=56&act=detail&idx="
    ),
    HANA_NEWS_MATERIAL(
            1,
            "남북하나재단",
            "보도자료",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=57",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=57&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=57&act=detail&idx="
    ),
    HANA_PRESS_RELEASE(
            1,
            "남북하나재단",
            "언론보도",
            "https://www.koreahana.or.kr/home/kor/notificationField/pressRelease/index.do?menuPos=58",
            "https://www.koreahana.or.kr/home/kor/notificationField/pressRelease/index.do?menuPos=58&pageIndex=",
            ""
    ),
    HANA_PUBLIC_NOTIFICATION(
            1,
            "남북하나재단",
            "공시송달",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=59",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=59&pageIndex=",
            "https://www.koreahana.or.kr/home/kor/board.do?menuPos=59&act=detail&idx="
    ),
    UNIKOREA_NOTICE(
            2,
            "통일부",
            "공지사항",
            "https://www.unikorea.go.kr/unikorea/notify/notice/",
            "https://www.unikorea.go.kr/unikorea/notify/notice/?mode=list&pageIdx=",
            "https://www.unikorea.go.kr/unikorea/notify/notice/?mode=view&cntId="
    ),
    UNIKOREA_RECRUIT(
            2,
            "통일부",
            "채용공고",
            "https://www.unikorea.go.kr/unikorea/notify/recruit/",
            "https://www.unikorea.go.kr/unikorea/notify/recruit/?mode=list&pageIdx=",
            "https://www.unikorea.go.kr/unikorea/notify/recruit/?mode=view&cntId="
    ),
    ;

    private final int sourceId;
    private final String source;
    private final String category;
    private final String baseUrl;
    private final String boardUrlPrefix;
    private final String detailUrlPrefix;
}

