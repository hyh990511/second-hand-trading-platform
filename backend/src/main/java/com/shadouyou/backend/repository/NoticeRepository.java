package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    Notice findByNoticeid(Integer noticeid);
}
