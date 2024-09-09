package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);

}