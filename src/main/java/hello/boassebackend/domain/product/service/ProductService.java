package hello.boassebackend.domain.product.service;

import hello.boassebackend.domain.product.dto.*;
import hello.boassebackend.domain.product.entity.Product;
import hello.boassebackend.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

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
                .data(ProductDetailResponse.Data.from(product))
                .build();
    }

    /**
     * 제품 등록
     */
    @Transactional
    public Long createProduct(ProductCreateRequest request) {
        Product product = Product.builder()
                .title(request.getTitle())
                .category(Product.ProductCategory.fromString(request.getCategory()))
                .image(request.getImage())
                .description(request.getDescription())
                .detail(request.getDetail())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("제품 등록 성공: productId={}, title=\"{}\"", savedProduct.getId(), savedProduct.getTitle());
        return savedProduct.getId();
    }

    /**
     * 제품 정보 수정
     */
    @Transactional
    public Long updateProduct(Long id, ProductCreateRequest request) {
        Product product = findProductByIdOrThrow(id);

        product.update(
                request.getTitle(),
                Product.ProductCategory.fromString(request.getCategory()),
                request.getImage(),
                request.getDescription(),
                request.getDetail()
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
        productRepository.delete(product);
        log.info("제품 삭제 완료: productId={}, title=\"{}\"", id, product.getTitle());
    }

    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품을 찾을 수 없습니다. id=" + id));
    }
}
