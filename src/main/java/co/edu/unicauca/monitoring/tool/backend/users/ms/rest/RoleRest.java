package co.edu.unicauca.monitoring.tool.backend.users.ms.rest;

import co.edu.unicauca.monitoring.tool.backend.users.ms.business.IRoleBusiness;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.RoleDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Role operations.
 */
@Tag(name = "Role Backend APIs", description = "API endpoints for managing user operations")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor = @__ (@Autowired))
public class RoleRest {

  private final IRoleBusiness roleBusiness;

  /**
   * Creates a new user.
   * @param payload The user data to be created.
   * @return A response containing the created user.
   */
  @PostMapping
  public ResponseEntity<ResponseDto<RoleDto>> createRole(@RequestBody RoleDto payload) {
    return roleBusiness.createRole(payload).of();
  }

  /**
   * Gets all users.
   * @return A response containing the list of all users.
   */
  @GetMapping
  public ResponseEntity<ResponseDto<List<RoleDto>>> getAllRoles() {
    return roleBusiness.getAllRoles().of();
  }

  /**
   * Gets a user by their ID.
   * @param roleId The ID of the user to fetch.
   * @return A response containing the user data.
   */
  @GetMapping("/{roleId}")
  public ResponseEntity<ResponseDto<RoleDto>> getRoleById(@PathVariable Long roleId) {
    return roleBusiness.getRoleById(roleId).of();
  }

  /**
   * Updates an existing user.
   * @param roleId The ID of the user to update.
   * @param payload The new user data to update.
   * @return A response containing the updated user data.
   */
  @PatchMapping("/{roleId}")
  public ResponseEntity<ResponseDto<RoleDto>> updateRole(@PathVariable Long roleId, @RequestBody RoleDto payload) {
    return roleBusiness.updateRole(roleId, payload).of();
  }

  /**
   * Deletes a user by their ID.
   * @param roleId The ID of the user to delete.
   * @return A response indicating the deletion status.
   */
  @DeleteMapping("/{roleId}")
  public ResponseEntity<ResponseDto<Void>> deleteRole(@PathVariable Long roleId) {
    return roleBusiness.deleteRole(roleId).of();
  }

}
