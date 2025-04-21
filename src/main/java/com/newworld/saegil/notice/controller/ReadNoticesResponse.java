package com.newworld.saegil.notice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.newworld.saegil.notice.service.NoticeDto;
import com.newworld.saegil.notice.service.ReadNoticesResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ReadNoticesResponse(

        @Schema(description = "공지사항 목록")
        List<ReadNoticeItemResponse> notices,

        @Schema(description = "마지막으로 조회된 공지사항 날짜 (다음 리스트 요청 시 사용할 lastDate 파라미터 값)", example = "2025-04-01")
        LocalDate lastResultDate,

        @Schema(description = "마지막으로 조회된 공지사항 ID (다음 리스트 요청 시 사용할 lastId 파라미터 값)", example = "101")
        Long lastResultId,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext
) {

    public static ReadNoticesResponse from(final ReadNoticesResult result) {
        final List<ReadNoticeItemResponse> readNoticeItemResponses = result.notices()
                                                                           .stream()
                                                                           .map(ReadNoticeItemResponse::from)
                                                                           .toList();

        return new ReadNoticesResponse(
                readNoticeItemResponses,
                result.lastResultDate(),
                result.lastResultId(),
                result.hasNext()
        );
    }

    public record ReadNoticeItemResponse(

            @Schema(description = "공지사항 식별자", example = "1")
            Long id,

            @Schema(description = "공지사항 제목", example = "2025년도 경남지역적응센터(하나센터) 채용 공고(~4.15)")
            String title,

            @Schema(description = "공지사항 내용", example = "공지사항 내용")
            String content,

            @Schema(description = "기관", example = "기관 이름")
            String source,

            @Schema(description = "공지사항 등록일", example = "2025.04.01.")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd.")
            LocalDate date,

            @Schema(description = "공지사항 URL", example = "https://www.koreahana.or.kr/home/kor/board.do?&menuPos=54&act=detail&idx=19193&boardPwd=&searchValue1=0&searchKeyword=&pageIndex=1")
            String webLink
    ) {

        public static ReadNoticeItemResponse from(final NoticeDto notice) {
            return new ReadNoticeItemResponse(
                    notice.id(),
                    notice.title(),
                    notice.content(),
                    notice.noticeType().getSource(),
                    notice.date(),
                    notice.webLink()
            );
        }
    }
}
