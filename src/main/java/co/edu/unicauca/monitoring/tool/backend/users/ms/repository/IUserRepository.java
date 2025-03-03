package co.edu.unicauca.monitoring.tool.backend.users.ms.repository;

import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository interface for User entities.
 * Extends JpaRepository to utilize built-in methods for CRUD operations.
 */
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(final String email);
    List<User> findAllByNameContainsIgnoreCase( final String name);
}
