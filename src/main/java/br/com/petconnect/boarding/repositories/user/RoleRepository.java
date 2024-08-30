package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findById(Long id);
}