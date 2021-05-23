package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Appeal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppealRepository extends JpaRepository<Appeal,Integer> {

    Page<Appeal> findAllByState(int i, Pageable pageRequest);
}
