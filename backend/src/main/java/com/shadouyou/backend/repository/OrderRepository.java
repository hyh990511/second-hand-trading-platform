package com.shadouyou.backend.repository;


import com.shadouyou.backend.entity.Order_;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order_,Integer> {
    List<Order_> findAllByUserid(Integer uid);
    Page<Order_> findAllByUserid(Integer userid, Pageable pageable);
    Order_ findByOrderid(Integer oid);
}
