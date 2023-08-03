package org.nimshub;

import org.nimshub.products.Product;
import org.nimshub.products.ProductService;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        ProductService productService = applicationContext.getInstance(ProductService.class);
        Product product = productService.getProductById(1);
        System.out.println(product);
    }
}