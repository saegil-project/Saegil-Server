package com.newworld.saegil.organization.service.crawler;

import com.newworld.saegil.organization.domain.Facility;
import com.newworld.saegil.organization.service.FacilityCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NationalSocialWelfareFacilityCrawler implements FacilityCrawler {

    @Value("${openapi.public-data.private-encoding-key}")
    private String privateEncodingKey;
    @Value("${openapi.public-data.national-social-welfare-facility.api-uri}")
    private String apiUri;

    private final RestTemplate restTemplate;

    @Override
    public List<Facility> crawl() {
        int page = 1;
        final List<Facility> totalFacilities = new ArrayList<>();
        while (true) {
            final URI requestUri = createRequestUri(page);
            try {
                final NationalSocialWelfareFacilityResponse response =
                        restTemplate.getForObject(requestUri, NationalSocialWelfareFacilityResponse.class);
                if (response.isItemEmpty()) {
                    break;
                }

                final List<Facility> facilities = response.getItems()
                                                          .stream()
                                                          .map(Item::toFacility)
                                                          .toList();
                totalFacilities.addAll(facilities);
            } catch (Exception e) {
                log.error("Error occurred while fetching page({}): {}", page, e.getMessage());
            }
            page++;
        }

        return totalFacilities;
    }

    private URI createRequestUri(final int page) {
        return UriComponentsBuilder.fromUriString(apiUri)
                                   .queryParam("serviceKey", privateEncodingKey)
                                   .queryParam("numOfRows", 1000)
                                   .queryParam("pageNo", page)
                                   .queryParam("_type", "json")
                                   .build(true)
                                   .toUri();
    }

    record NationalSocialWelfareFacilityResponse(
            Response response
    ) {
        public boolean isItemEmpty() {
            return response == null || response.body == null || response.body.items == null
                    || response.body.items.item == null || response.body.items.item.isEmpty();
        }

        public List<Item> getItems() {
            return response.body.items.item;
        }
    }

    record Response(
            Body body
    ) {
    }

    record Body(
            Items items,
            int numOfRows,
            int pageNo,
            int totalCount
    ) {
    }

    record Items(
            List<Item> item
    ) {
    }

    record Item(
            String fcltCd, // 시설코드
            String fcltNm, // 시설명
            String fcltAddr, // 시설주소
            String fcltDtl_1Addr, // 시설상세1주소
            String fcltDtl_2Addr, // 시설상세2주소
            String fcltTelno, // 시설전화번호
            String cfbClCd, // 업종분류코드
            String cfbNm // 업종명
    ) {

        public Facility toFacility() {
            final String address = fcltAddr + " " + fcltDtl_1Addr + " " + fcltDtl_2Addr;

            return new Facility(
                    fcltNm,
                    fcltTelno,
                    address,
                    address,
                    null,
                    null
            );
        }
    }
}
