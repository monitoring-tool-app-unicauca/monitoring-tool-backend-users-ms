package co.edu.unicauca.monitoring.tool.backend.users.ms.rest;

import co.edu.unicauca.monitoring.tool.backend.users.ms.business.IUserBusiness;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.PasswordRecoveryDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnCreate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnUpdate;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ResponseDto<UserDto>> createUser(@RequestBody @Validated(OnCreate.class) UserDto userDto) {
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
     * Gets a user by their email.
     * @param email The email of the user to fetch.
     * @return A response containing the user data.
     */
    @GetMapping("/by-email")
    public ResponseEntity<ResponseDto<UserDto>> getUserByEmail(@RequestParam String email) {
        return userBusiness.getUserByEmail(email).of();
    }

    /**
     * Gets a user by their email.
     * @param name The name of the users to fetch.
     * @return A response containing the user data.
     */
    @GetMapping("/by-name")
    public ResponseEntity<ResponseDto<List<UserDto>>> getUserByNameContains(@RequestParam String name) {
        return userBusiness.getUsersByNameContains(name).of();
    }

    /**
     * Updates an existing user.
     * @param userId The ID of the user to update.
     * @param userDto The new user data to update.
     * @return A response containing the updated user data.
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserDto>> updateUser(@PathVariable Long userId,
                                                           @RequestBody @Validated(OnUpdate.class) UserDto userDto) {
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

    /**
     * Uploads a profile image for the specified user.
     *
     * @param userId The ID of the user.
     * @param file The image file to be uploaded.
     * @return A response indicating the success or failure of the upload.
     */
    @PatchMapping("/{userId}/profile-image")
    public ResponseEntity<ResponseDto<Void>> uploadProfileImage(@PathVariable Long userId,
                                                                @RequestParam MultipartFile file) {
        return userBusiness.uploadProfileImage(userId, file).of();
    }

    /**
     * Uploads a profile image for the specified user.
     *
     * @return A response indicating the success or failure of the upload.
     */
    @PatchMapping("/reset-password")
    public ResponseEntity<ResponseDto<UserDto>> updatePassword(@RequestBody @Validated(OnUpdate.class)
                                                                PasswordRecoveryDto payload) {
        return userBusiness.resetPassword(payload).of();
    }

    /**
     * Retrieves the profile image of the specified user.
     *
     * @param userId The ID of the user.
     * @return The profile image as a byte array with JPEG content type.
     */
    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId) {
        ResponseDto<byte[]> response = userBusiness.getProfileImage(userId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(response.getData());
    }

    /**
     * Gets users by their IDs.
     * @param ids The list of user IDs to fetch.
     * @return A response containing the user data.
     */
    @GetMapping("/by-ids")
    public ResponseEntity<ResponseDto<List<UserDto>>> getUsersByIds(@RequestParam List<Long> ids) {
        return userBusiness.getUsersByIds(ids).of();
    }


    /**
     * Initiates the password reset process for a user.
     * @param email The email of the user who wants to reset their password.
     * @return A response indicating the success or failure of the password reset initiation.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDto<Void>> forgotPassword(@RequestParam String email) {
        return userBusiness.sendMailForgotPassword(email).of();
    }

}
