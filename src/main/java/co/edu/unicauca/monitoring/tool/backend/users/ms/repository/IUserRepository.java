package co.edu.unicauca.monitoring.tool.backend.users.ms.repository;

import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * A repository interface for User entities.
 * Extends JpaRepository to utilize built-in methods for CRUD operations.
 */
public interface IUserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their document number.
     *
     * @param documentNumber The document number to search for.
     * @return An Optional wrapping the User found or an empty Optional if not found.
     */
    Optional<User> findByDocumentNumber(final String documentNumber);
}
