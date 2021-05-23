package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Goods;
import com.shadouyou.backend.entity.GoodsUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods,Integer> {

    Goods findByGoodsid(Integer Id);

//    Page <Goods> findByNameContainingAndIspassAndIsexamineAndIssaleOrderByRealasetime(String name,Integer ispass,Integer isexmine,Integer issale,Pageable pageable);

    Page <Goods> findByNameContainingAndIsexamineAndIssaleOrderByRealasetime(String name,Integer isexmine,Integer issale,Pageable pageable);

    Page<Goods> findAllByIsexamine(Integer i, Pageable pageRequest);

//    List<Goods> findAllByIsexamineAndIspassAndIssale(Integer isexamine,Integer ispass,Integer issale);

    List<Goods> findAllByIsexamineAndIssale(Integer isexamine,Integer issale);

    //    Page<Goods> findAllByIsexamineAndIspassAndIssale(Integer isexamine,Integer ispass,Integer issale,Pageable pageable);

    Page<Goods> findAllByIsexamineAndIssale(Integer isexamine,Integer issale,Pageable pageable);

    //模糊查询
    @Query(value = "select * from Goods where name like concat('%',:name,'%') ",nativeQuery = true)
    List<Goods> findByName(@Param("name") String name);

    @Query(value = "select  g.goodsid as goodsID,  g.name as name, g.title as title, g.text as text, g.userid as userID, g.goodsprice as  goodsPrice, g.sendprice as sendprice, g.exchangebook as exchangeBook, g.tag as tag, g.childtag as childTag, g.realasetime as realaseTime, g.isexamine as isExamine, g.ascription as ascription, g.nopassreason as nopassReason, g.issale as isSale, g.imgfirst as imgfirst, g.imgsecond as imgsecond, g.imgthird as imgthird, u.creditrating as creditRating, u.nickname as nickname from goods g, user u where g.userid = u.userid and g.goodsid = :id ", nativeQuery = true)
     GoodsUser findAllGoodsUserById(@Param("id") Integer id);

    @Query(value = "select  g.goodsid as goodsID,  g.name as name, g.title as title, g.text as text, g.userid as userID, g.goodsprice as  goodsPrice, g.sendprice as sendprice, g.exchangebook as exchangeBook, g.tag as tag, g.childtag as childTag, g.realasetime as realaseTime, g.isexamine as isExamine, g.ascription as ascription, g.nopassreason as nopassReason, g.issale as isSale, g.imgfirst as imgfirst, g.imgsecond as imgsecond, g.imgthird as imgthird, u.creditrating as creditRating, u.nickname as nickname from goods g, user u where g.userid = u.userid and g.goodsid = :id ", nativeQuery = true)
    GoodsUser findAllGoodsUserByIdAndBooks(@Param("id") Integer id);

    @Query(value = "select  g.goodsid as goodsID,  g.name as name, g.title as title, g.text as text, g.userid as userID, g.goodsprice as  goodsPrice, g.sendprice as sendprice, g.exchangebook as exchangeBook, g.tag as tag, g.childtag as childTag, g.realasetime as realaseTime, g.isexamine as isExamine, g.ascription as ascription, g.nopassreason as nopassReason, g.issale as isSale, g.imgfirst as imgfirst, g.imgsecond as imgsecond, g.imgthird as imgthird, u.creditrating as creditRating, u.nickname as nickname from goods g, user u where g.userid = u.userid and g.name like concat('%',:name,'%') and g.isexamine = 1 and g.issale = 0 and g.ascription like :ascription ", nativeQuery = true)
    List<GoodsUser> findAllGoodsUserByName(@Param("name") String name,@Param("ascription") String ascription);

    Integer countByUseridAndIssale(Integer uid, int i);

    List<Goods> findAllByUserid(int i);

    @Query(value = "select g.goodsid as goodsID,  g.name as name, g.title as title, g.text as text, g.userid as userID, g.goodsprice as  goodsPrice, g.sendprice as sendprice, g.exchangebook as exchangeBook, g.tag as tag, g.childtag as childTag, g.realasetime as realaseTime, g.isexamine as isExamine, g.examinetime as examineTime,g.ascription as ascription, g.nopassreason as nopassReason, g.issale as isSale, g.imgfirst as imgfirst, g.imgsecond as imgsecond, g.imgthird as imgthird ,u.creditrating as creditRating, u.nickname as nickname from goods g, user u where g.userid = u.userid and g.ascription = :ascription and g.issale= 0 and g.isexamine = 1 limit :size ",nativeQuery = true)
    List<GoodsUser> findAllGoodsByAscription(@Param("size") Integer size,@Param("ascription") String ascription);

    Page<Goods> findAllByUserid(Integer uid, Pageable pageRequest);







    @Query(value = "select g.goodsid as goodsID,  g.name as name, g.title as title, g.text as text, g.userid as userID, g.goodsprice as  goodsPrice, g.sendprice as sendprice, g.exchangebook as exchangeBook, g.tag as tag, g.childtag as childTag, g.realasetime as realaseTime, g.isexamine as isExamine, g.examinetime as examineTime,g.ascription as ascription, g.nopassreason as nopassReason, g.issale as isSale, g.imgfirst as imgfirst, g.imgsecond as imgsecond, g.imgthird as imgthird ,u.creditrating as creditRating, u.nickname as nickname from Goods g , user u where g.userid = u.userid and g.ascription = :ascription and g.issale= 0 and g.isexamine = 1 and g.Tag = :tN",nativeQuery = true)
    List<GoodsUser> findAllByTagAndIssaleAndAscriptionAndIsexamine(@Param("tN") String tN,@Param("ascription") String ascription);
    @Query(value = "select g.goodsid as goodsID,  g.name as name, g.title as title, g.text as text, g.userid as userID, g.goodsprice as  goodsPrice, g.sendprice as sendprice, g.exchangebook as exchangeBook, g.tag as tag, g.childtag as childTag, g.realasetime as realaseTime, g.isexamine as isExamine, g.examinetime as examineTime,g.ascription as ascription, g.nopassreason as nopassReason, g.issale as isSale, g.imgfirst as imgfirst, g.imgsecond as imgsecond, g.imgthird as imgthird ,u.creditrating as creditRating, u.nickname as nickname from Goods g , user u where g.userid = u.userid and g.ascription = :ascription and g.issale= 0 and g.isexamine = 1  and g.childtag = :tN",nativeQuery = true)
    List<GoodsUser> findAllByChildtagAndIssaleAndIsexamine(@Param("tN") String tN,@Param("ascription") String ascription);
}
