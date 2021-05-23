package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByAscriptionAndTagname(String ascription,String tagname);

    List<Tag> findAllByAscriptionOrderByHotDesc(String ascription);
}
