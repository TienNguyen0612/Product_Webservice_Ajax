package codegym.controller;

import codegym.model.Product;
import codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    //lấy full database
    @GetMapping
    public ResponseEntity<Iterable<Product>> showAll() {
        Iterable<Product> products = productService.findAll();
        if (!products.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //lấy 1 đối tượng theo id
    @GetMapping("/{id}")
    public ResponseEntity<Product> showOne(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        return productOptional.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //tạo 1 đối tượng
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product productCreate = productService.save(product);
        return new ResponseEntity<>(productCreate, HttpStatus.CREATED);
    }

    //cập nhật 1 đối tượng
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        product.setId(productOptional.get().getId());
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

    //xoá 1 đối tượng theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.delete(id);
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<Product>> searchAllByName(@RequestParam("search") String search) {
        Iterable<Product> products = productService.findAllByName(search);
        if (!products.iterator().hasNext()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
