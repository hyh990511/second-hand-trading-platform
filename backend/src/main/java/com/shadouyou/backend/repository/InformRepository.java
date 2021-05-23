package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Inform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformRepository extends JpaRepository<Inform,Integer> {
    Page<Inform> findAllByTypeAndState(Integer type, Integer state, Pageable pageRequest);
}
