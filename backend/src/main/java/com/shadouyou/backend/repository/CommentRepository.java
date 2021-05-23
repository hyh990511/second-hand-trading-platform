package com.shadouyou.backend.repository;

import com.shadouyou.backend.entity.Comment;
import com.shadouyou.backend.entity.CommentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    List<Comment> findAllByUserid(Integer uid);

    @Query(value="select  c.commentid as commentID ,c.userid as userID , c.goodsid as goodsid, c.releasetime as releaseTime,c.text as text,u.nickname as nickname ,u.accountnumber as accountNumber, u.images as images from Comment c,User u where c.goodsid = :goodsid and c.userid = u.userid order by c.releaseTime", nativeQuery=true,countQuery = "select count(c.userid) from Comment c,User u where c.goodsid = :goodsid and c.userid = u.userid order by c.releaseTime")
    Page<CommentUser> findCommentUserByGoodsid(@Param("goodsid") Integer goodsid, Pageable pageable);

}
