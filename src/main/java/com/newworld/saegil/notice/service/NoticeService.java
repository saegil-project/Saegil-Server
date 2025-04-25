package com.newworld.saegil.notice.service;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private static final int FIRST_PAGE = 0;

    private final NoticeRepository noticeRepository;

    public ReadNoticesResult readAll(
            final String query,
            final Long sourceId,
            final Long lastId,
            final int size
    ) {
        final Pageable pageable = PageRequest.of(FIRST_PAGE, size);
        final List<Notice> notices = noticeRepository.findAllByCursor(query, sourceId, lastId, pageable);
        final Long lastResultId = notices.isEmpty() ? null : notices.getLast().getId();
        final boolean hasNext = notices.size() == size;

        return new ReadNoticesResult(
                notices.stream()
                       .map(NoticeDto::from)
                       .toList(),
                lastResultId,
                hasNext
        );
    }
}
