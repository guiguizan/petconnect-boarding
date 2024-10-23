package br.com.petconnect.boarding.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private String sku; // Código de identificação do produto (Stock Keeping Unit)

    @Column(nullable = false)
    private String category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags; // Lista de tags associadas ao produto

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images; // Lista de URLs de imagens do produto

    @Column(nullable = false)
    private Boolean active; // Status de ativo/inativo do produto

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime; // Data de criação do produto

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime; // Data da última atualização do produto

}