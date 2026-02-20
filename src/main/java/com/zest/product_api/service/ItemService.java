package com.zest.product_api.service;

import org.springframework.data.domain.*;

import com.zest.product_api.dto.ItemRequest;
import com.zest.product_api.dto.ItemResponse;

public interface ItemService {
    ItemResponse addItem(Long productId, ItemRequest request);
    Page<ItemResponse> getItemsByProduct(Long productId, Pageable pageable);
    ItemResponse updateItem(Long id, ItemRequest request);
    void deleteItem(Long id);
}