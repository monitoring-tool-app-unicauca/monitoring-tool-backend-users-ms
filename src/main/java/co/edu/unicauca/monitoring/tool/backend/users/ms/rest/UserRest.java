package co.edu.unicauca.monitoring.tool.backend.users.ms.rest;

import co.edu.unicauca.monitoring.tool.backend.users.ms.business.IUserBusiness;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for User operations.
 */
@Tag(name = "User Backend APIs", description = "API endpoints for managing user operations")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__ (@Autowired))
public class UserRest {

    private final IUserBusiness userBusiness;

    /**
     * Creates a new user.
     * @param userDto The user data to be created.
     * @return A response containing the created user.
     */
    @PostMapping
    public ResponseEntity<ResponseDto<UserDto>> createUser(@RequestBody UserDto userDto) {
        return userBusiness.createUser(userDto).of();
    }

    /**
     * Gets all users.
     * @return A response containing the list of all users.
     */
    @GetMapping
    public ResponseEntity<ResponseDto<List<UserDto>>> getAllUsers() {
        return userBusiness.getAllUsers().of();
    }

    /**
     * Gets a user by their ID.
     * @param userId The ID of the user to fetch.
     * @return A response containing the user data.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserDto>> getUserById(@PathVariable Long userId) {
        return userBusiness.getUserById(userId).of();
    }

    /**
     * Updates an existing user.
     * @param userId The ID of the user to update.
     * @param userDto The new user data to update.
     * @return A response containing the updated user data.
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserDto>> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return userBusiness.updateUser(userId, userDto).of();
    }

    /**
     * Deletes a user by their ID.
     * @param userId The ID of the user to delete.
     * @return A response indicating the deletion status.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto<Void>> deleteUser(@PathVariable Long userId) {
        return userBusiness.deleteUser(userId).of();
    }


}
