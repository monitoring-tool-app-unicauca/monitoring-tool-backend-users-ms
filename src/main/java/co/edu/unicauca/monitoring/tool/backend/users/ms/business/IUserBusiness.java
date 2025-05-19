package co.edu.unicauca.monitoring.tool.backend.users.ms.business;


import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.PasswordRecoveryDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * An interface for Customer Business operations.
 */
public interface IUserBusiness {

    /**
     * Creates a new user.
     *
     * @param payload The user information to be created.
     * @return A ResponseDto containing the created UserDto.
     */
    ResponseDto<UserDto> createUser(final UserDto payload);

    /**
     * Retrieves a UserDto based on the provided user ID.
     *
     * @param userId The user ID used to fetch the user information.
     * @return A ResponseDto containing the UserDto information if found, or an appropriate response if not found.
     */
    ResponseDto<UserDto> getUserById(final Long userId);

    /**
     * Updates an existing user.
     *
     * @param payload The user information to be updated.
     * @return A ResponseDto containing the updated UserDto.
     */
    ResponseDto<UserDto> updateUser(final Long id, final UserDto payload);

    /**
     * Deletes a user by their ID.
     *
     * @param userId The user ID to be deleted.
     * @return A ResponseDto indicating the success or failure of the deletion.
     */
    ResponseDto<Void> deleteUser(final Long userId);

    /**
     * Retrieves all users.
     *
     * @return A ResponseDto containing a list of UserDto objects representing all users.
     */
    ResponseDto<Page<UserDto>> getAllUsers(Pageable pageable);


    /**
     * Retrieves all users by role.
     *
     * @return A ResponseDto containing a list of UserDto objects representing all users by role.
     */
    ResponseDto<Page<UserDto>> getAllUsersByRole(Long roleId, Pageable pageable);

    /**
     * Uploads a profile image for a user.
     *
     * @param userId The ID of the user whose profile image is being uploaded.
     * @param file The image file to be uploaded.
     * @return A response indicating the success or failure of the upload.
     */
    ResponseDto<Void> uploadProfileImage(Long userId, MultipartFile file);

    /**
     * Retrieves the profile image of a user.
     *
     * @param userId The ID of the user whose profile image is being retrieved.
     * @return A response containing the profile image as a byte array.
     */
    ResponseDto<byte[]> getProfileImage(Long userId);

    /**
     * Retrieves a UserDto based on the provided email.
     *
     * @param email The email used to fetch the user information.
     * @return A ResponseDto containing the UserDto information if found, or an appropriate response if not found.
     */
    ResponseDto<UserDto> getUserByEmail(String email);

    /**
     * Retrieves a list of users whose name contains the specified substring.
     *
     * @param name The substring to search for in usernames.
     * @return A ResponseDto containing a list of matching UserDto objects.
     */
    ResponseDto<List<UserDto>> getUsersByNameContains(String name);

    /**
     * Retrieves a list of UserDto based on the provided user IDs.
     *
     * @param payload The list of user IDs used to fetch the user information.
     * @return A ResponseDto containing a list of UserDto information if found, or an appropriate response if not found.
     */
    ResponseDto<List<UserDto>> getUsersByIds(final List<Long> payload);

    /**
     * Sends a forgot password email to the specified email address.
     *
     * @param email The email address to send the forgot password email to.
     * @return A ResponseDto indicating the success or failure of the operation.
     */
    ResponseDto<Void> sendMailForgotPassword(final String email);

    /**
     * Resets the password based on the provided recovery information.
     *
     * @param payload The PasswordRecoveryDto containing recovery information.
     * @return A ResponseDto containing the updated UserDto information.
     */
    ResponseDto<UserDto> resetPassword(final PasswordRecoveryDto payload);

    /**
     * Loads user details based on the username.
     *
     * @param username The username of the user to be loaded.
     * @return UserDetails of the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

