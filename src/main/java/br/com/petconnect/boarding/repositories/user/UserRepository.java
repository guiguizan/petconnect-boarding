package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByCpf(String cpf);
    Boolean existsByEmail(String email);
    @Query("SELECT u FROM br.com.petconnect.boarding.domain.User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmail(@Param("email") String email);


    @Query("SELECT u FROM br.com.petconnect.boarding.domain.User u JOIN u.roles r WHERE r.name = :roleName")
    Page<User> findByRoleName(String roleName, Pageable pageable);
}
