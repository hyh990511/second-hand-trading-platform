package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Administrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AdministratorRepositoryTest {
    @Autowired
    private AdministratorRepository administratorRepository;

    @Test
    void findByAccount() {
        //按条件查询 findBy（字段名首字母大写）  findByAccount(String s)相当于 where account = s
        Administrator administrator = administratorRepository.findByAccount("1999");
        System.out.println(administrator.getAccesslevel());
        System.out.println(administrator.getPassword());
        System.out.println(administrator.getNickname());


    }
}