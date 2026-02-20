package com.zest.product_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.product_api.dto.ItemRequest;
import com.zest.product_api.dto.ItemResponse;
import com.zest.product_api.service.ItemService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    private WebApplicationContext context;

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

    private ItemResponse buildItemResponse() {
        ItemResponse response = new ItemResponse();
        response.setId(1L);
        response.setProductId(1L);
        response.setProductName("Test Product");
        response.setQuantity(10);
        return response;
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addItem_ShouldReturn201() throws Exception {
        ItemRequest request = new ItemRequest();
        request.setQuantity(10);

        when(itemService.addItem(anyLong(), any(ItemRequest.class)))
            .thenReturn(buildItemResponse());

        mockMvc.perform(post("/api/v1/items/product/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.quantity").value(10));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getItems_ShouldReturn200() throws Exception {
        when(itemService.getItemsByProduct(anyLong(), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(buildItemResponse())));

        mockMvc.perform(get("/api/v1/items/product/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateItem_ShouldReturn200() throws Exception {
        ItemRequest request = new ItemRequest();
        request.setQuantity(20);

        ItemResponse updated = buildItemResponse();
        updated.setQuantity(20);

        when(itemService.updateItem(anyLong(), any(ItemRequest.class)))
            .thenReturn(updated);

        mockMvc.perform(put("/api/v1/items/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.quantity").value(20));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteItem_ShouldReturn200() throws Exception {
        doNothing().when(itemService).deleteItem(1L);

        mockMvc.perform(delete("/api/v1/items/1").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }
}