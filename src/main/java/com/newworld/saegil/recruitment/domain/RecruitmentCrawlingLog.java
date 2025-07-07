package com.newworld.saegil.recruitment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
@Table(name = "recruitment_crawling_log")
public class RecruitmentCrawlingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentInfoSource infoSource;

    @Column(nullable = false)
    private LocalDate crawlingDate;

    public RecruitmentCrawlingLog(final RecruitmentInfoSource infoSource, final LocalDate crawlingDate) {
        this.infoSource = infoSource;
        this.crawlingDate = crawlingDate;
    }
}
