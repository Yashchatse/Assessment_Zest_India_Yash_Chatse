package com.zest.product_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.product_api.dto.ProductRequest;
import com.zest.product_api.dto.ProductResponse;
import com.zest.product_api.service.ItemService;
import com.zest.product_api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean private ProductService productService;
    @MockitoBean private ItemService itemService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    private ProductResponse buildProductResponse() {
        ProductResponse r = new ProductResponse();
        r.setId(1L);
        r.setProductName("Test Product");
        r.setCreatedBy("admin");
        r.setCreatedOn(LocalDateTime.now());
        return r;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAll_ShouldReturn200() throws Exception {
        when(productService.getAllProducts(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(buildProductResponse())));

        mockMvc.perform(get("/api/v1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getById_ShouldReturn200() throws Exception {
        when(productService.getProductById(1L)).thenReturn(buildProductResponse());

        mockMvc.perform(get("/api/v1/products/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.productName").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void create_ShouldReturn201() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setProductName("Test Product");

        when(productService.createProduct(any(ProductRequest.class), anyString()))
            .thenReturn(buildProductResponse());

        mockMvc.perform(post("/api/v1/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.productName").value("Test Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void delete_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}