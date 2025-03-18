package co.edu.unicauca.monitoring.tool.backend.users.ms.business;

import co.edu.unicauca.monitoring.tool.backend.users.ms.config.MessageLoader;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.exception.BusinessRuleException;
import co.edu.unicauca.monitoring.tool.backend.users.ms.mapper.IUserMapper;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IRoleRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IUserRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__ (@Autowired))
public class UserBusinessImpl implements IUserBusiness {
    private static final Logger logger = LoggerFactory.getLogger(UserBusinessImpl.class);

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseDto<UserDto> createUser(UserDto payload) {
        validateEmailUniqueness(payload.getEmail());
        User user = userMapper.toDomain(payload);
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        updateUserData(user, payload);
        return saveAndRespond(user, HttpStatus.CREATED, MessagesConstants.IM002);
    }

    @Override
    public ResponseDto<UserDto> updateUser(Long id, UserDto payload) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, id)));
        if (!payload.getEmail().equalsIgnoreCase(user.getEmail())) {
            validateEmailUniqueness(payload.getEmail());
        }
        updateUserData(user, payload);
        return saveAndRespond(user, HttpStatus.OK, MessagesConstants.IM003);
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                MessagesConstants.EM019,
                MessageLoader.getInstance().getMessage(MessagesConstants.EM019, email));
        }
    }

    private void updateUserData(User user, UserDto payload) {
        user.setName(payload.getName());
        user.setLastName(payload.getLastName());
        user.setPhoneNumber(payload.getPhoneNumber());
        user.setEmail(payload.getEmail());
        if (Objects.nonNull(payload.getRoleIds()) && !payload.getRoleIds().isEmpty())
            user.setRoles(roleRepository.findAllById(payload.getRoleIds()));
    }

    private ResponseDto<UserDto> saveAndRespond(User user, HttpStatus status, String messageCode) {
        User userSaved = userRepository.save(user);
        return new ResponseDto<>(status.value(),
            MessageLoader.getInstance().getMessage(messageCode),
            userMapper.toDto(userSaved));
    }



    @Override
    public ResponseDto<UserDto> getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, userId)));
        logger.info("User has been found!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001),
            userMapper.toDto(user));
    }


    @Override
    public ResponseDto<Void> deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, userId)));

        userRepository.delete(user);
        logger.info("User has been deleted!");

        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM003));
    }

    @Override
    public ResponseDto<List<UserDto>> getAllUsers() {
        List<UserDto> usersResponse = userRepository.findAll().stream()
            .map(userMapper::toDto)
            .toList();
        logger.info("All users have been fetched!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001), usersResponse);
    }

    @Override
    public ResponseDto<Void> uploadProfileImage(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, userId)));
        try {
            user.setProfileImage(file.getBytes());
            userRepository.save(user);
            return new ResponseDto<>(HttpStatus.OK.value(),
                MessageLoader.getInstance().getMessage(MessagesConstants.IM003));
        } catch (IOException e) {
            throw new BusinessRuleException(HttpStatus.OK.value(),
                MessagesConstants.EM017, MessageLoader.getInstance().getMessage(MessagesConstants.EM017, userId));
        }
    }

    @Override
    public ResponseDto<byte[]> getProfileImage(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
                MessageLoader.getInstance().getMessage(MessagesConstants.EM002, userId), MessagesConstants.EM002));
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001), user.getProfileImage());
    }

    @Override
    public ResponseDto<UserDto> getUserByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, email)));
      logger.info("User found by email: {}", email);
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001),
            userMapper.toDto(user));
    }

    @Override
    public ResponseDto<List<UserDto>> getUsersByNameContains(String name) {
        List<UserDto> usersResponse = userRepository.findAllByNameContainsIgnoreCase(name).stream()
            .map(userMapper::toDto)
            .toList();
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001), usersResponse);
    }

    @Override
    public ResponseDto<List<UserDto>> getUsersByIds(List<Long> payload) {
        List<UserDto> usersResponse = userRepository.findAllById(payload).stream()
                .map(userMapper::toDto)
                .toList();
        return new ResponseDto<>(HttpStatus.OK.value(),
                MessageLoader.getInstance().getMessage(MessagesConstants.IM001), usersResponse);
    }
}
