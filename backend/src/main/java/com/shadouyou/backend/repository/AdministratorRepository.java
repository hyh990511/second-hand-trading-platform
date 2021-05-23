package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.AdmiPage;
import com.shadouyou.backend.entity.Administrator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdministratorRepository extends JpaRepository<Administrator,Integer> {
    Administrator findByAccount(String s);
    Administrator findByAdministratorid(int id);
    Page<Administrator> findAllByAccesslevel(Integer level, Pageable pageRequest);

    @Query(value = "select new com.shadouyou.backend.entity.AdmiPage(admi) from Administrator as admi")
    List<AdmiPage> pagefindAll();

    @Query(value = "select new com.shadouyou.backend.entity.AdmiPage(admi) from Administrator as admi where admi.accesslevel = :level")
    List<AdmiPage> pagefindAllByLevel(@Param("level") Integer level);
}
