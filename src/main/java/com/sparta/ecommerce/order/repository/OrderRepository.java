package com.sparta.ecommerce.order.repository;

import com.sparta.ecommerce.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.buyerEmail = :buyerEmail")
    Page<Order> findAllByBuyerEmail(String buyerEmail, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.product p WHERE p.id = :productId")
    Page<Order> findAllByProductId(Long productId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.product p WHERE p.id = :productId AND " +
            "(o.createdAt < :cursorCreatedAt OR " +
            "(o.createdAt = :cursorCreatedAt AND o.id < :cursorId)) " +
            "ORDER BY o.createdAt DESC, o.id DESC")
    List<Order> findAllByProductIdWithCursor(Long productId, LocalDateTime cursorCreatedAt,
            Long cursorId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.product p WHERE o.buyerEmail = :buyerEmail " +
            "AND (o.createdAt < :cursorCreatedAt OR " +
            "(o.createdAt = :cursorCreatedAt AND o.id < :cursorId)) " +
            "ORDER BY o.createdAt DESC, o.id DESC")
    List<Order> findAllByBuyerEmailWithCursor(String buyerEmail, LocalDateTime cursorCreatedAt,
            Long cursorId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.product p WHERE o.buyerEmail = :buyerEmail " +
            "ORDER BY o.createdAt DESC, o.id DESC")
    List<Order> findByBuyerEmailForFirstPage(String buyerEmail, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.product p WHERE p.id = :productId " +
            "ORDER BY o.createdAt DESC, o.id DESC")
    List<Order> findByProductIdForFirstPage(Long productId, Pageable pageable);
}
