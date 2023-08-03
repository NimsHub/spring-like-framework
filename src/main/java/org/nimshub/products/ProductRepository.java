package org.nimshub.products;

import org.nimshub.annotations.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ProductRepository {
    List<Product> productList = new ArrayList<>();

    ProductRepository() {
        productList.add(new Product(1, "cheese", "20"));
        productList.add(new Product(2, "butter", "30"));
        productList.add(new Product(3, "milk", "50"));
        productList.add(new Product(4, "chocolate", "10"));
    }

    public Product getProduct(Integer id) {
        return productList.stream()
                .filter(i -> Objects.equals(i.id(), id))
                .toList()
                .get(0);
    }
}
