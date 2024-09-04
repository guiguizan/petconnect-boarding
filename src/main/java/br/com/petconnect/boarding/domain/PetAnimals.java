package br.com.petconnect.boarding.domain;


import br.com.petconnect.boarding.util.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "pet_animal")
public class PetAnimals {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pet_id")
    private Long idPet;

    @Column(name = "name")
    private String name;

    private String color;

    private Integer age;

    private String breed;

    private String petType;

    @Column(name = "birthdate")
    private LocalDate birthDate;


    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    public void setAge() {

        this.age = DateUtils.calculateAge(this.birthDate);
    }

}
