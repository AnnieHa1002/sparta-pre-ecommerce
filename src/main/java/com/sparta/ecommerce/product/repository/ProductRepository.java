package com.sparta.ecommerce.product.repository;

import com.sparta.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p " +
            "WHERE p.id = :productId AND p.orders IS NOT EMPTY AND p.isDeleted IS FALSE")
    boolean existsByIdAndOrdersIsNotEmptyAndIsDeletedIsFalse(Long productId);


    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :count WHERE p.id = :id AND p.stock >= :count")
    int decreaseStock(Long id, int count);

    Page<Product> findAllByIsDeletedIsFalse(Pageable pageable);


    Page<Product> findAllBySellerNameAndIsDeletedIsFalse(String sellerName, Pageable pageable);
}
