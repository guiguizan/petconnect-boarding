package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findAllDistinctCategories();
}
