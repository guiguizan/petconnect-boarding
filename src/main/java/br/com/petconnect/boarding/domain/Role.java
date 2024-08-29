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
@Entity(name = "pet_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idRole;

    @Column(name = "role_name")
    private String name;

    @Column(name = "role_description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;




}
