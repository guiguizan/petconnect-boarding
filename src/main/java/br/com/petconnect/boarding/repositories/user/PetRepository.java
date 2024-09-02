package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.PetAnimals;

import br.com.petconnect.boarding.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository

public interface PetRepository extends JpaRepository<PetAnimals, Long> {
    Page<PetAnimals> findByUser(User user, Pageable pageable);

    Optional<PetAnimals> findByIdPetAndUser(Long id, User user);
}
