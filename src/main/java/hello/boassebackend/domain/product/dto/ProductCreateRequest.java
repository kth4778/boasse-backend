package hello.boassebackend.domain.product.dto;

import lombok.Data;

@Data
public class ProductCreateRequest {
    private String title;
    private String category;
    private String image;
    private String description;
    private String detail;
}