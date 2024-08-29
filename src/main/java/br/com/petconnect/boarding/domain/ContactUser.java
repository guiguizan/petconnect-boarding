package br.com.petconnect.boarding.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "pet_contact")
public class ContactUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_contact")
    private Long idContact;

    @Column(name = "contact_type")
    private String type;

    @Column(name = "contact_value")
    private String contactValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)  // Define a chave estrangeira
    private User user;  // Associa o contato a um usuário
}
