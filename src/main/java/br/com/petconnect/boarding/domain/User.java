package br.com.petconnect.boarding.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "pet_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(name = "name_user")
    private String nmUser;

    @Column(name = "email_user")
    private String email;

    @Column(name = "user_cpf")
    private String cpf;

    @Column(name = "user_password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactUser> contacts;


    @ManyToMany
    @JoinTable(
            name = "user_roles", // Nome da tabela intermediária
            joinColumns = @JoinColumn(name = "user_id"), // Nome da coluna que referencia o User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Nome da coluna que referencia o Role
    )
    private List<Role> roles;


}
