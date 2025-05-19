package co.edu.unicauca.monitoring.tool.backend.users.ms.repository;

import co.edu.unicauca.monitoring.tool.backend.users.ms.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * A repository interface for User entities.
 * Extends JpaRepository to utilize built-in methods for CRUD operations.
 */
public interface IRoleRepository extends JpaRepository<Role, Long> {

  /**
   * Finds a role by its name, ignoring case sensitivity.
   *
   * @param name The name of the role to search for.
   * @return An {@link Optional} containing the role if found, or empty if not found.
   */
  Optional<Role> findByNameIgnoreCase(final String name);


}
