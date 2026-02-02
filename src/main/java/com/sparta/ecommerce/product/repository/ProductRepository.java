package com.sparta.ecommerce.product.repository;

import com.sparta.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
            "WHERE p.id = :productId AND p.orders IS NOT EMPTY AND p.isDeleted IS FALSE")
    boolean existsByIdAndOrdersIsNotEmptyAndIsDeletedIsFalse(Long productId);


    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :count WHERE p.id = :id AND p.stock >= :count")
    int decreaseStock(Long id, int count);

    Page<Product> findAllByIsDeletedIsFalse(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
            "AND (p.createdAt < :cursorCreatedAt " +
            "OR (p.createdAt = :cursorCreatedAt AND p.id < :cursorId)) " +
            "ORDER BY p.createdAt DESC, p.id DESC")
    List<Product> findAllWithCursor(LocalDateTime cursorCreatedAt, Long cursorId,
            Pageable pageable);

    Page<Product> findAllBySellerNameAndIsDeletedIsFalse(String sellerName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
            "ORDER BY p.createdAt DESC, p.id DESC")
    List<Product> findFirstPage(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
            "AND p.sellerName =  :sellerName AND (p.createdAt < :cursorCreatedAt " +
            "OR (p.createdAt = :cursorCreatedAt AND p.id < :cursorId)) " +
            "ORDER BY p.createdAt DESC, p.id DESC")
    List<Product> findAllBySellerNameWithCursor(String sellerName, LocalDateTime cursorCreatedAt,
            Long cursorId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
            "AND p.sellerName =  :sellerName ORDER BY p.createdAt DESC, p.id DESC")
    List<Product> findBySellerNameForFirstPage(String sellerName, Pageable pageable);
}
