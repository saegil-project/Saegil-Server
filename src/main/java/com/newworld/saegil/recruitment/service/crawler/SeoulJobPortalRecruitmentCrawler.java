package com.newworld.saegil.recruitment.service.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newworld.saegil.recruitment.domain.Recruitment;
import com.newworld.saegil.recruitment.domain.RecruitmentInfoSource;
import com.newworld.saegil.recruitment.service.RecruitmentCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeoulJobPortalRecruitmentCrawler implements RecruitmentCrawler {

    @Value("${openapi.seoul-data.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    @Override
    public RecruitmentInfoSource getSupportingRecruitmentInfoSource() {
        return RecruitmentInfoSource.SEOUL_DATA_SEOUL_JOB_PORTAL;
    }

    @Override
    public List<Recruitment> crawl(final LocalDate requestDate) {
        int startIndex = 1;
        int endIndex = 500;
        int requestCount = 500;
        final List<Recruitment> totalRecruitments = new ArrayList<>();
        while (true) {
            final String requestUri = createRequestUri(requestDate, startIndex, endIndex);
            final SeoulJobPortalRecruitmentResponse response =
                    restTemplate.getForObject(requestUri, SeoulJobPortalRecruitmentResponse.class);

            if (response == null || response.isItemEmpty()) {
                break;
            }
            final List<Recruitment> recruitments = response.getItems()
                                                           .stream()
                                                           .map(RecruitmentItem::toRecruitment)
                                                           .toList();
            totalRecruitments.addAll(recruitments);

            startIndex += requestCount;
            endIndex += requestCount;
        }

        return totalRecruitments;
    }

    @NotNull
    private String createRequestUri(final LocalDate requestDate, final int startIndex, final int endIndex) {
        final String blank = URLEncoder.encode(" ", StandardCharsets.UTF_8);

        return UriComponentsBuilder.fromUriString("http://openapi.seoul.go.kr:8088")
                                   .pathSegment(
                                           serviceKey, "json", "GetJobInfo",
                                           String.valueOf(startIndex),
                                           String.valueOf(endIndex),
                                           blank, blank, blank, blank,
                                           requestDate.toString()
                                   )
                                   .build()
                                   .toUriString();
    }

    record SeoulJobPortalRecruitmentResponse(
            @JsonProperty("GetJobInfo")
            Response getJobInfo
    ) {

        public boolean isItemEmpty() {
            return getJobInfo == null || getJobInfo.rows == null || getJobInfo.rows.isEmpty();
        }

        public List<RecruitmentItem> getItems() {
            return getJobInfo.rows;
        }
    }

    record Response(
            @JsonProperty("list_total_count")
            int listTotalCount,

            @JsonProperty("row")
            List<RecruitmentItem> rows
    ) {
    }

    record RecruitmentItem(
            @JsonProperty("JO_REGIST_NO") String jobRegistNumber, // 구인등록번호
            @JsonProperty("JO_SJ") String title, // 구인 제목
            @JsonProperty("CMPNY_NM") String companyName, // 기업명칭
            @JsonProperty("BASS_ADRES_CN") String workplaceAddress, // 근무지주소
            @JsonProperty("HOLIDAY_NM") String weeklyWorkdays, // ex. 주 5일 근무
            @JsonProperty("WORK_TIME_NM") String workTime, // 근무시간 ex "(근무시간) (오전) 9시 00분 ~ (오후) 6시 00분"
            @JsonProperty("HOPE_WAGE") String wage, // 급여조건
            @JsonProperty("JO_REG_DT") String jobRegisterDate, // 등록일
            @JsonProperty("RCEPT_CLOS_NM") String receptionCloseDate // 마감일
    ) {

        public Recruitment toRecruitment() {
            return new Recruitment(
                    jobRegistNumber,
                    RecruitmentInfoSource.SEOUL_DATA_SEOUL_JOB_PORTAL,
                    title,
                    LocalDate.parse(jobRegisterDate).atStartOfDay(),
                    parseReceptionCloseDate(receptionCloseDate),
                    weeklyWorkdays,
                    workTime,
                    wage,
                    "",
                    workplaceAddress
            );
        }

        private LocalDateTime parseReceptionCloseDate(String receptionCloseDate) {
            // "마감일 (2025-07-15)" 형식의 문자열에서 날짜 정보 파싱
            Pattern pattern = Pattern.compile("\\((\\d{4}-\\d{2}-\\d{2}(?:\\s+\\d{2}:\\d{2})?)\\)");
            Matcher matcher = pattern.matcher(receptionCloseDate);
            if (!matcher.find()) {
                return null;
            }
            String dateTimeStr = matcher.group(1);

            if (dateTimeStr.contains(" ")) { // 날짜와 시간이 모두 있는 경우
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return LocalDateTime.parse(dateTimeStr, formatter);
            }

            // 날짜만 있는 경우
            LocalDate date = LocalDate.parse(dateTimeStr);
            return date.atStartOfDay();
        }
    }
}
