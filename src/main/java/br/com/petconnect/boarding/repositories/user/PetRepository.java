package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.PetAnimals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PetRepository extends JpaRepository<PetAnimals, Long> {
}
