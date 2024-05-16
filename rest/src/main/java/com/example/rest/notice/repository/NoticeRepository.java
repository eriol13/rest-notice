package com.example.rest.notice.repository;

import com.example.common.domain.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
