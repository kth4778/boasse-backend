package hello.boassebackend.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(length = 500)
    private String image; // 이미지 URL

    @Column(length = 500)
    private String description; // 짧은 설명

    @Lob
    @Column(columnDefinition = "TEXT")
    private String detail; // 상세 설명

    @Lob
    @Column(columnDefinition = "TEXT")
    private String specs; // JSON String

    @Lob
    @Column(columnDefinition = "TEXT")
    private String features; // JSON String

    @Column(name = "is_main_featured", nullable = false)
    private boolean isMainFeatured;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Product(String title, ProductCategory category, String image, String description, String detail, String specs, String features, boolean isMainFeatured) {
        this.title = title;
        this.category = category;
        this.image = image;
        this.description = description;
        this.detail = detail;
        this.specs = specs;
        this.features = features;
        this.isMainFeatured = isMainFeatured;
    }

    public void update(String title, ProductCategory category, String image, String description, String detail, String specs, String features, boolean isMainFeatured) {
        this.title = title;
        this.category = category;
        if (image != null && !image.isEmpty()) {
            this.image = image;
        }
        this.description = description;
        this.detail = detail;
        this.specs = specs;
        this.features = features;
        this.isMainFeatured = isMainFeatured;
    }

    public enum ProductCategory {
        SMART_MOBILITY("Smart Mobility"),
        SMART_FACTORY("Smart Factory"),
        SMART_FARM("Smart Farm"),
        SMART_BUILDING("Smart Building");

        private final String value;

        ProductCategory(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
        
        public static ProductCategory fromString(String text) {
            for (ProductCategory b : ProductCategory.values()) {
                if (b.value.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}