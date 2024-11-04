package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND LOWER(p.category) = LOWER(:category)")
    Page<Product> searchByNameAndCategory(@Param("name") String name, @Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    Page<Product> filterByCategory(@Param("category") String category, Pageable pageable);


    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllDistinctCategories();
}
