package hello.boassebackend.domain.product.service;

import hello.boassebackend.domain.notice.entity.Attachment;
import hello.boassebackend.domain.product.dto.*;
import hello.boassebackend.domain.product.entity.Product;
import hello.boassebackend.domain.product.repository.ProductRepository;
import hello.boassebackend.global.util.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStore fileStore;

    /**
     * 제품 목록 조회
     */
    public ProductListResponse getProducts() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        
        List<ProductItem> items = products.stream()
                .map(ProductItem::from)
                .collect(Collectors.toList());

        return ProductListResponse.builder()
                .success(true)
                .data(items)
                .build();
    }

    /**
     * 제품 상세 조회
     */
    public ProductDetailResponse getProductDetail(Long id) {
        Product product = findProductByIdOrThrow(id);
        return ProductDetailResponse.builder()
                .success(true)
                .data(ProductItem.from(product))
                .build();
    }

    /**
     * 제품 등록
     */
    @Transactional
    public Long createProduct(ProductCreateRequest request) throws IOException {
        String imageUrl = "";
        
        // 1. 파일 업로드 우선 처리
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            Attachment attachment = fileStore.storeFile(request.getImage());
            if (attachment != null) {
                imageUrl = attachment.getUrl();
            }
        } 
        // 2. 파일이 없으면 직접 입력된 URL 사용
        else if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            imageUrl = request.getImageUrl();
        }

        Product.ProductCategory category = Product.ProductCategory.fromString(request.getCategory());
        if (category == null) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + request.getCategory());
        }

        Product product = Product.builder()
                .title(request.getTitle())
                .category(category)
                .image(imageUrl)
                .description(request.getDescription())
                .detail(request.getDetail())
                .specs(request.getSpecs())
                .features(request.getFeatures())
                .isMainFeatured(request.getIsMainFeatured() != null && request.getIsMainFeatured())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("제품 등록 성공: productId={}, title=\"{}\"", savedProduct.getId(), savedProduct.getTitle());
        return savedProduct.getId();
    }

    /**
     * 제품 정보 수정
     */
    @Transactional
    public Long updateProduct(Long id, ProductUpdateRequest request) throws IOException {
        Product product = findProductByIdOrThrow(id);

        String imageUrl = product.getImage(); // 기본값은 기존 이미지
        
        // 1. 새 파일이 업로드된 경우
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            // 기존 파일 삭제
            deleteOldFile(product.getImage());
            
            Attachment attachment = fileStore.storeFile(request.getImage());
            if (attachment != null) {
                imageUrl = attachment.getUrl();
            }
        }
        // 2. 파일은 없지만 새 이미지 URL이 문자열로 들어온 경우 (외부 링크 등)
        else if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            // URL이 변경되었다면 기존 파일 정리 (필요 시) 후 업데이트
            if (!request.getImageUrl().equals(product.getImage())) {
                deleteOldFile(product.getImage());
                imageUrl = request.getImageUrl();
            }
        }

        Product.ProductCategory category = Product.ProductCategory.fromString(request.getCategory());
        if (category == null) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + request.getCategory());
        }

        product.update(
                request.getTitle(),
                category,
                imageUrl,
                request.getDescription(),
                request.getDetail(),
                request.getSpecs(),
                request.getFeatures(),
                request.getIsMainFeatured() != null && request.getIsMainFeatured()
        );

        log.info("제품 수정 완료: productId={}", id);
        return product.getId();
    }

    /**
     * 제품 삭제
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductByIdOrThrow(id);
        
        // 이미지 파일 삭제
        deleteOldFile(product.getImage());
        
        productRepository.delete(product);
        log.info("제품 삭제 완료: productId={}, title=\"{}\"", id, product.getTitle());
    }

    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품을 찾을 수 없습니다. id=" + id));
    }
    
    private void deleteOldFile(String fileUrl) {
        if (fileUrl != null && fileUrl.contains("/api/v1/files/")) {
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            fileStore.deleteFile(filename);
        }
    }
}