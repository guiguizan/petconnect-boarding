package br.com.petconnect.boarding.repositories.user;

import br.com.petconnect.boarding.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TokenRepository extends JpaRepository<PasswordResetToken,Long> {
   Optional <PasswordResetToken> findByToken(String token);
}
