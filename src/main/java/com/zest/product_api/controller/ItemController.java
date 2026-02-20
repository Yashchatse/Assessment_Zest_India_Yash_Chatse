package com.zest.product_api.controller;

import com.zest.product_api.dto.ApiResponse;
import com.zest.product_api.dto.ItemRequest;
import com.zest.product_api.dto.ItemResponse;
import com.zest.product_api.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "3. Items", description = "Item operations")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/product/{productId}")
    @Operation(summary = "Add item to a product")
    public ResponseEntity<ApiResponse<ItemResponse>> addItem(
            @PathVariable Long productId,
            @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(itemService.addItem(productId, request), "Item added"));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get all items for a product")
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> getItems(
            @PathVariable Long productId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(
            ApiResponse.success(itemService.getItemsByProduct(productId, pageable), "Items fetched"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(
            ApiResponse.success(itemService.updateItem(id, request), "Item updated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an item")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Item deleted"));
    }
}