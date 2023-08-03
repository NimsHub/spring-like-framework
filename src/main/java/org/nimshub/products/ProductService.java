package org.nimshub.products;

import org.nimshub.annotations.Autowired;
import org.nimshub.annotations.Component;

@Component
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product getProductById(Integer id) {
        return productRepository.getProduct(id);
    }
}
