package com.newworld.saegil.facility.service;

import com.newworld.saegil.facility.domain.BusinessName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// 20320 / 35410
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FacilityBusinessNameClassifier {

    private static final Map<BusinessName, Set<String>> categoryKeywords = new LinkedHashMap<>();

    static {
        categoryKeywords.put(BusinessName.CHILDREN_WELFARE, Set.of("아동", "어린이", "청소년", "보육", "방과후", "지역아동센터", "키움센터", "아이들"));
        categoryKeywords.put(BusinessName.ELDERLY_WELFARE, Set.of("노인", "요양", "실버", "주간보호", "재가", "데이케어", "치매", "어르신", "양로원", "경로", "시니어"));
        categoryKeywords.put(BusinessName.DISABILITY_WELFARE, Set.of("장애", "재활", "보호작업장", "자립생활", "점자", "농아", "수어", "뇌성마비"));
        categoryKeywords.put(BusinessName.WOMEN_FAMILY_WELFARE, Set.of("여성", "가족", "가정", "한부모", "미혼모", "다문화", "부녀자"));
        categoryKeywords.put(BusinessName.MEDICAL_WELFARE_EQUIPMENT, Set.of("의료", "간호", "복지용구", "기저귀", "의료기기"));
    }

    public static BusinessName classify(
            final String businessName,
            final String facilityName
    ) {
        for (Map.Entry<BusinessName, Set<String>> entry : categoryKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (businessName.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }

        for (Map.Entry<BusinessName, Set<String>> entry : categoryKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (facilityName.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }

        return BusinessName.OTHERS;
    }
}
