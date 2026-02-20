package com.zest.product_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zest.product_api.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByProductId(Long productId);
    Page<Item> findByProductId(Long productId, Pageable pageable);
}
