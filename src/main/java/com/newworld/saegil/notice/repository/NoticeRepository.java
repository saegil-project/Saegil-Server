package com.newworld.saegil.notice.repository;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
    SELECT n FROM Notice n
    WHERE n.date = (
        SELECT MAX(n2.date)
        FROM Notice n2
        WHERE n2.noticeType = :noticeType
    ) AND n.noticeType = :noticeType
    """)
    List<Notice> findAllLatestNoticeByType(final NoticeType noticeType);

    @Query("""
        SELECT n FROM Notice n
        WHERE (:query IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:sourceId IS NULL OR n.sourceId = :sourceId)
          AND (
            (:lastDate IS NULL)
            OR (n.date < :lastDate)
            OR (n.date = :lastDate AND n.id < :lastId)
          )
        ORDER BY n.date DESC, n.id DESC
        """)
    List<Notice> findAllByCursor(
            final String query,
            final Long sourceId,
            final LocalDate lastDate,
            final Long lastId,
            final Pageable pageable
    );
}
