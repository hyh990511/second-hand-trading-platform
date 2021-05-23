package com.shadouyou.backend.repository;
import java.util.List;
import java.util.Map;
import com.shadouyou.backend.entity.GoodsUser;
import com.shadouyou.backend.entity.User;

import com.shadouyou.backend.entity.UserPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
        User findUserByAccountnumber(String s);
        User findByUserid(Integer uid);

//        @Query(value = "select \"title\" as common, u.id as id, u.name as name, a.id as addressId, a.address as addressName from goods g, user u where g.userid = u.userid", nativeQuery = true)
//        public List<GoodsUser> findAllUserAddress();

        @Query(value ="SELECT new com.shadouyou.backend.entity.UserPage(u) from User as u ")
        List<UserPage> pagefindAll();



//        @Query(value = "select \"title\" as common, u.id as id, u.name as name, a.id as addressId, a.address as addressName from user u, address a where u.id = a.user_id and u.id=1", nativeQuery = true)
//        public Map<String, Object> findAllUserAddressByMap();

}
