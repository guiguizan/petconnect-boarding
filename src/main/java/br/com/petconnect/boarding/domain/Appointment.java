package br.com.petconnect.boarding.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "pet_appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idAppointment;
    private String serviceType;
    private String petType;
    private LocalDate appointmentDate;
    private LocalTime   appointmentTime;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "idPet", nullable = false)
    private PetAnimals pet;


    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;


}
