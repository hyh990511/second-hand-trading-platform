package com.shadouyou.backend.service.Impl;

import com.shadouyou.backend.entity.Goods;
import com.shadouyou.backend.repository.GoodsRepository;
import com.shadouyou.backend.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public List<Goods> getAllGoods(){
        return goodsRepository.findAll();
    };

   @Override
    public Goods getGoodsById(Integer gid){
        return goodsRepository.findByGoodsid(gid);
    };

    @Override
    public Goods addGoods(Goods goods){
        return goodsRepository.save(goods);
    };

    @Override
    public Goods updateGoods(Goods goods){
        return goodsRepository.save(goods);
    };

    @Override
    public void deleteGoodsById(Integer gid){
      goodsRepository.deleteById(gid);
    }


    ;
}
