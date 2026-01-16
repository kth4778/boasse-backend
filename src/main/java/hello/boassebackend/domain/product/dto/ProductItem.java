package hello.boassebackend.domain.product.dto;

import hello.boassebackend.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static ProductItem from(Product product) {
        return ProductItem.builder()
                .id(product.getId())
                .title(product.getTitle())
                .category(product.getCategory().getValue())
                .image(product.getImage())
                .description(product.getDescription())
                .build();
    }
}