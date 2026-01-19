package hello.boassebackend.domain.product.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.boassebackend.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    private Long id;
    private String title;
    private String category;
    private String image;
    private String description;
    private String detail;
    private List<Map<String, Object>> specs;
    private List<Map<String, Object>> features;
    private boolean isMainFeatured;
    private LocalDateTime createdAt;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProductItem from(Product product) {
        return ProductItem.builder()
                .id(product.getId())
                .title(product.getTitle())
                .category(product.getCategory().getValue())
                .image(product.getImage())
                .description(product.getDescription())
                .detail(product.getDetail())
                .specs(parseJsonList(product.getSpecs()))
                .features(parseJsonList(product.getFeatures()))
                .isMainFeatured(product.isMainFeatured())
                .createdAt(product.getCreatedAt())
                .build();
    }

    private static List<Map<String, Object>> parseJsonList(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, List.class);
        } catch (JsonProcessingException e) {
            log.error("JSON parsing error for Product specs/features", e);
            return Collections.emptyList();
        }
    }
}
