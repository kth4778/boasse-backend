package hello.boassebackend.domain.product.controller;

import hello.boassebackend.domain.product.dto.ProductCreateRequest;
import hello.boassebackend.domain.product.dto.ProductDetailResponse;
import hello.boassebackend.domain.product.dto.ProductListResponse;
import hello.boassebackend.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 제품 목록 조회
     */
    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts(
            jakarta.servlet.http.HttpServletRequest request) {
        ProductListResponse response = productService.getProducts();
        return ResponseEntity.ok(response);
    }

    /**
     * 제품 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
        ProductDetailResponse response = productService.getProductDetail(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 제품 등록
     */
    @PostMapping
    public ResponseEntity<ProductDetailResponse> createProduct(@RequestBody ProductCreateRequest requestDto, jakarta.servlet.http.HttpServletRequest request) {
        Long productId = productService.createProduct(requestDto);
        ProductDetailResponse response = productService.getProductDetail(productId);
        
        request.setAttribute("logData", "productId=" + productId + ", title=" + requestDto.getTitle());
        return ResponseEntity.created(URI.create("/api/v1/products/" + productId))
                .body(response);
    }

    /**
     * 제품 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> updateProduct(@PathVariable Long id, @RequestBody ProductCreateRequest requestDto, jakarta.servlet.http.HttpServletRequest request) {
        Long productId = productService.updateProduct(id, requestDto);
        ProductDetailResponse response = productService.getProductDetail(productId);

        request.setAttribute("logData", "productId=" + productId);
        return ResponseEntity.ok(response);
    }

    /**
     * 제품 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id, jakarta.servlet.http.HttpServletRequest request) {
        productService.deleteProduct(id);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "삭제되었습니다.");

        request.setAttribute("logData", "productId=" + id);
        return ResponseEntity.ok(response);
    }
}