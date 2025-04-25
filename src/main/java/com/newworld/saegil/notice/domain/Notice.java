package com.newworld.saegil.notice.domain;

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
@ToString(of = {"id", "title", "noticeType", "date", "webLink"})
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private int sourceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeType noticeType;

    @Column(nullable = true)
    private LocalDate date;

    @Column(nullable = false)
    private String webLink;

    public Notice(
            final String title,
            final String content,
            final NoticeType noticeType,
            final LocalDate date,
            final String webLink
    ) {
        this.title = title;
        this.content = content;
        this.sourceId = noticeType.getSourceId();
        this.noticeType = noticeType;
        this.date = date;
        this.webLink = webLink;
    }

    public boolean isBefore(final LocalDate lastDate) {
        if (lastDate == null) {
            return false;
        }
        return this.date.isBefore(lastDate);
    }

    public boolean hasSameTitle(final Notice other) {
        return this.title.equals(other.title);
    }
}
