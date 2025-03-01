package co.edu.unicauca.monitoring.tool.backend.users.ms.business;


import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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
    ResponseDto<List<UserDto>> getAllUsers();

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
}

