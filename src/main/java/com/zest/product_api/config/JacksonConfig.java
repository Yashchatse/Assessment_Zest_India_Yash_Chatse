package com.zest.product_api.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Handle LocalDateTime, LocalDate, etc.
        mapper.registerModule(new JavaTimeModule());

        // Don't serialize dates as timestamps (use ISO strings instead)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Don't include null fields in JSON response
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Set timezone
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));

        return mapper;
    }
}