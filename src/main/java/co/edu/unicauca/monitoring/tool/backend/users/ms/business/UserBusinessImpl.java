package co.edu.unicauca.monitoring.tool.backend.users.ms.business;

import co.edu.unicauca.monitoring.tool.backend.users.ms.config.MessageLoader;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.exception.BusinessRuleException;
import co.edu.unicauca.monitoring.tool.backend.users.ms.mapper.IUserMapper;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IUserRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import java.io.IOException;
import java.util.List;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseDto<UserDto> createUser(UserDto payload) {
        if (userRepository.findByEmailIgnoreCase(payload.getEmail()).isPresent()) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                MessagesConstants.EM019,
                MessageLoader.getInstance().getMessage(MessagesConstants.EM019, payload.getEmail()));
        }
        User user = userMapper.toDomain(payload);
        user.setPassword(passwordEncoder.encode(payload.getPassword()));
        User userSaved = userRepository.save(user);
        logger.info("User has been created!");
        return new ResponseDto<>(HttpStatus.CREATED.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM002),
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
    public ResponseDto<UserDto> updateUser(Long id, UserDto payload) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, id)));

        user.setFirstName(payload.getFirstName());
        user.setSecondName(payload.getSecondName());
        user.setFirstLastName(payload.getFirstLastName());
        user.setSecondLastName(payload.getSecondLastName());
        user.setPhoneNumber(payload.getPhoneNumber());
        user.setEmail(payload.getEmail());

        User userSaved = userRepository.save(user);
        logger.info("User has been updated!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM003),
            userMapper.toDto(userSaved));
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
}
