package com.zest.product_api.mapper;

import com.zest.product_api.dto.ItemResponse;
import com.zest.product_api.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
            .id(item.getId())
            .productId(item.getProduct().getId())
            .productName(item.getProduct().getProductName())
            .quantity(item.getQuantity())
            .build();
    }
}