package hello.boassebackend.domain.product.repository;

import hello.boassebackend.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Product.ProductCategory category);
}