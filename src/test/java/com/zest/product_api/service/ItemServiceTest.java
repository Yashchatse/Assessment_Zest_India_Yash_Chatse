package com.zest.product_api.service;

import com.zest.product_api.dto.ItemRequest;
import com.zest.product_api.dto.ItemResponse;
import com.zest.product_api.entity.Item;
import com.zest.product_api.entity.Product;
import com.zest.product_api.exception.ResourceNotFoundException;
import com.zest.product_api.mapper.ItemMapper;
import com.zest.product_api.repository.ItemRepository;
import com.zest.product_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock private ItemRepository itemRepository;
    @Mock private ProductRepository productRepository;
    @Mock private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Product product;
    private Item item;
    private ItemRequest itemRequest;
    private ItemResponse itemResponse;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setProductName("Test Product");

        item = new Item();
        item.setId(1L);
        item.setProduct(product);
        item.setQuantity(10);

        itemRequest = new ItemRequest();
        itemRequest.setQuantity(10);

        itemResponse = new ItemResponse();
        itemResponse.setId(1L);
        itemResponse.setProductId(1L);
        itemResponse.setQuantity(10);
    }

    @Test
    void addItem_WhenProductExists_ShouldReturnItemResponse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toResponse(item)).thenReturn(itemResponse);

        ItemResponse result = itemService.addItem(1L, itemRequest);

        assertNotNull(result);
        assertEquals(10, result.getQuantity());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItem_WhenProductNotFound_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> itemService.addItem(99L, itemRequest));
    }

    @Test
    void updateItem_WhenExists_ShouldReturnUpdated() {
        ItemRequest updateRequest = new ItemRequest();
        updateRequest.setQuantity(20);

        ItemResponse updatedResponse = new ItemResponse();
        updatedResponse.setId(1L);
        updatedResponse.setQuantity(20);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toResponse(item)).thenReturn(updatedResponse);

        ItemResponse result = itemService.updateItem(1L, updateRequest);

        assertNotNull(result);
        assertEquals(20, result.getQuantity());
    }

    @Test
    void deleteItem_WhenExists_ShouldDelete() {
        when(itemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(itemRepository).deleteById(1L);

        assertDoesNotThrow(() -> itemService.deleteItem(1L));
        verify(itemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteItem_WhenNotFound_ShouldThrowException() {
        when(itemRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
            () -> itemService.deleteItem(99L));
    }
}