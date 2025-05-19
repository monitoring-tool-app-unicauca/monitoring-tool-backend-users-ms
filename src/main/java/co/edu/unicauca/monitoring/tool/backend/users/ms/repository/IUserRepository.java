package co.edu.unicauca.monitoring.tool.backend.users.ms.repository;

import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * A repository interface for User entities.
 * Extends JpaRepository to utilize built-in methods for CRUD operations.
 */
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(final String email);
    List<User> findAllByNameContainsIgnoreCase( final String name);
    Page<User> findByRoles_RoleId(Long roleId, Pageable pageable);

}
