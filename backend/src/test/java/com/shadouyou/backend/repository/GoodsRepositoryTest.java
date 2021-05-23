package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Goods;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class GoodsRepositoryTest {
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    void findAll(){
        System.out.println(goodsRepository.findAll());
    }

}
