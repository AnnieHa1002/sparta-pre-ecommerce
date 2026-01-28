package com.sparta.ecommerce.order.repository;

import com.sparta.ecommerce.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.buyerEmail = :buyerEmail")
    Page<Order> findAllByBuyerEmail(String buyerEmail, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.product p WHERE p.id = :productId")
    Page<Order> findAllByProductId(Long productId, Pageable pageable);
}
