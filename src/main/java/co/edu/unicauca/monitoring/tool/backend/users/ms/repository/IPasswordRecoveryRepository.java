package co.edu.unicauca.monitoring.tool.backend.users.ms.repository;

import co.edu.unicauca.monitoring.tool.backend.users.ms.model.PasswordRecovery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IPasswordRecoveryRepository extends JpaRepository<PasswordRecovery, String> {
    Optional<PasswordRecovery> findFirstByEmailAndExpiresAfterOrderByExpiresDesc(String email, LocalDateTime expiresBefore);
}
