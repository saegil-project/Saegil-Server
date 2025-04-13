package com.newworld.saegil.notice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NoticeType {
    ;

    private final int sourceId;
    private final String source;
    private final String category;
    private final String baseUrl;
    private final String boardUrlPrefix;
    private final String detailUrlPrefix;
}

