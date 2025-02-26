package co.edu.unicauca.monitoring.tool.backend.users.ms.repository;

import co.edu.unicauca.monitoring.tool.backend.users.ms.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository interface for User entities.
 * Extends JpaRepository to utilize built-in methods for CRUD operations.
 */
public interface IRoleRepository extends JpaRepository<Role, Long> {
}
