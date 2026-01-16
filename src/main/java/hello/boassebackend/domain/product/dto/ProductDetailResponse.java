package hello.boassebackend.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.boassebackend.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private boolean success;
    private Data data;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Long id;
        private String title;
        private String category;
        private String image;
        private String description;
        private String detail;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        public static Data from(Product product) {
            return Data.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .category(product.getCategory().getValue())
                    .image(product.getImage())
                    .description(product.getDescription())
                    .detail(product.getDetail())
                    .createdAt(product.getCreatedAt())
                    .build();
        }
    }
}