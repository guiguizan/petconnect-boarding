package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserCpf(String cpf);

    Boolean existsByUserEmail(String email);
}
