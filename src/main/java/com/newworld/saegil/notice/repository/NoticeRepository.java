package com.newworld.saegil.notice.repository;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
