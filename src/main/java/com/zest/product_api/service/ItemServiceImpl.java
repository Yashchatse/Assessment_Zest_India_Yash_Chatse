package com.zest.product_api.service;

import com.zest.product_api.dto.*;
import com.zest.product_api.entity.Item;
import com.zest.product_api.entity.Product;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.mapper.ItemMapper;
import com.zest.product_api.repository.ItemRepository;
import com.zest.product_api.repository.ProductRepository;
import com.zest.product_api.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository,
                           ProductRepository productRepository,
                           ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemResponse addItem(Long productId, ItemRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Product not found with id: " + productId));

        Item item = Item.builder()
            .product(product)
            .quantity(request.getQuantity())
            .build();

        return itemMapper.toResponse(itemRepository.save(item));
    }

    @Override
    public Page<ItemResponse> getItemsByProduct(Long productId, Pageable pageable) {
        if (!productRepository.existsById(productId))
            throw new ResourceNotFoundException("Product not found with id: " + productId);

        return itemRepository.findByProductId(productId, pageable)
            .map(itemMapper::toResponse);
    }

    @Override
    public ItemResponse updateItem(Long id, ItemRequest request) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Item not found with id: " + id));

        item.setQuantity(request.getQuantity());
        return itemMapper.toResponse(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id))
            throw new ResourceNotFoundException("Item not found with id: " + id);
        itemRepository.deleteById(id);
    }
}