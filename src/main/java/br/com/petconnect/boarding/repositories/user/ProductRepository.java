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

    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :name, '%'))) AND LOWER(p.category) = LOWER(:category)")
    Page<Product> searchByNameAndCategory(@Param("name") String name, @Param("category") String category, Pageable pageable);

    // Busca apenas por nome ou descrição
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> searchByNameOrDescription(@Param("name") String name, Pageable pageable);

    // Filtra apenas pela categoria
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    Page<Product> filterByCategory(@Param("category") String category, Pageable pageable);


    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllDistinctCategories();
}
