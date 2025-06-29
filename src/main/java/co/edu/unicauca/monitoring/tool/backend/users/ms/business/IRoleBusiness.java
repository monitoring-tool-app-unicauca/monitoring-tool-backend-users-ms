package co.edu.unicauca.monitoring.tool.backend.users.ms.business;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.RoleDto;
import java.util.List;

public interface IRoleBusiness {

  /**
   * Creates a new user.
   *
   * @param payload The user information to be created.
   * @return A ResponseDto containing the created RoleDto.
   */
  ResponseDto<RoleDto> createRole(final RoleDto payload);

  /**
   * Retrieves a RoleDto based on the provided user ID.
   *
   * @param id The user ID used to fetch the user information.
   * @return A ResponseDto containing the RoleDto information if found, or an appropriate response if not found.
   */
  ResponseDto<RoleDto> getRoleById(final Long id);

  /**
   * Updates an existing user.
   *
   * @param payload The user information to be updated.
   * @return A ResponseDto containing the updated RoleDto.
   */
  ResponseDto<RoleDto> updateRole(final Long id, final RoleDto payload);

  /**
   * Deletes a user by their ID.
   *
   * @param id The user ID to be deleted.
   * @return A ResponseDto indicating the success or failure of the deletion.
   */
  ResponseDto<Void> deleteRole(final Long id);

  /**
   * Retrieves all users.
   *
   * @return A ResponseDto containing a list of RoleDto objects representing all users.
   */
  ResponseDto<List<RoleDto>> getAllRoles();
}
