package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String Email);
}
