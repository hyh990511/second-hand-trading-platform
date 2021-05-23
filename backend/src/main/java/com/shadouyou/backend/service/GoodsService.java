package com.shadouyou.backend.service;

import com.shadouyou.backend.entity.Goods;

import java.util.List;

public interface  GoodsService{
    List<Goods> getAllGoods();


    Goods addGoods(Goods goods);

    Goods updateGoods(Goods goods);

    void deleteGoodsById(Integer gid);


    Goods getGoodsById(Integer gid);

}