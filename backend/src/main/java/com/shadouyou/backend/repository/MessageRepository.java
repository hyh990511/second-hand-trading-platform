package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    Page<Message> findAllByReceiverid(Integer id, Pageable pageRequest);

    List<Message> findAllByReceiverid(Integer id);
    Integer countByReceiveridAndIsread(Integer uid, int i);
}
