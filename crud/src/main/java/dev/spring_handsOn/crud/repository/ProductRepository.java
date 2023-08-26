package dev.spring_handsOn.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.spring_handsOn.crud.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
