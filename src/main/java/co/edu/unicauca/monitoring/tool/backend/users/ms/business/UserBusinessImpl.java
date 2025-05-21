package co.edu.unicauca.monitoring.tool.backend.users.ms.business;

import co.edu.unicauca.monitoring.tool.backend.users.ms.config.MessageLoader;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.PasswordRecoveryDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.PasswordRecoveryNotificationDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.To;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.WelcomePasswordNotificationDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.exception.BusinessRuleException;
import co.edu.unicauca.monitoring.tool.backend.users.ms.mapper.IUserMapper;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.PasswordRecovery;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import co.edu.unicauca.monitoring.tool.backend.users.ms.publisher.IPasswordRecoveryPublisher;
import co.edu.unicauca.monitoring.tool.backend.users.ms.publisher.IWelcomePasswordPublisher;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IPasswordRecoveryRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IRoleRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IUserRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.PasswordGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__ (@Autowired))
public class UserBusinessImpl implements IUserBusiness, UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserBusinessImpl.class);

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IPasswordRecoveryPublisher passwordRecoveryPublisher;
    private final IPasswordRecoveryRepository passwordRecoveryRepository;
    private final IWelcomePasswordPublisher welcomePasswordPublisher;
    @Value("${domain.password.notification.url}")
    private String domainNotificationUrl;
    @Value("${domain.password.notification.expires}")
    private Integer tokenExpires;

    @Override
    public ResponseDto<UserDto> createUser(UserDto payload) {
        validateEmailUniqueness(payload.getEmail());
        User user = userMapper.toDomain(payload);
        var generatedPassword = PasswordGeneratorUtil.generatePassword();
        user.setPassword(passwordEncoder.encode(generatedPassword));
        updateUserData(user, payload);
        var userSaved = saveAndRespond(user, HttpStatus.CREATED, MessagesConstants.IM002);
        this.welcomePasswordPublisher.publish(WelcomePasswordNotificationDto.builder()
                .generatedPassword(generatedPassword)
                .recipient(To.builder().name(userSaved.getData().getName())
                        .email(userSaved.getData().getEmail())
                        .build()).build());
        return userSaved;
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
    public ResponseDto<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<UserDto> usersResponse = userRepository.findAll(pageable)
            .map(userMapper::toDto);
        logger.info("All users have been fetched!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001), usersResponse);
    }

    @Override
    public ResponseDto<Page<UserDto>> getAllUsersByRole(Long roleId, Pageable pageable) {
        Page<UserDto> usersResponse = userRepository.findByRoles_RoleId(roleId, pageable)
                .map(userMapper::toDto);
        logger.info("All users by role have been fetched!");
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

    @Override
    @Transactional
    public ResponseDto<Void> sendMailForgotPassword(final String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                        MessagesConstants.EM002, MessageLoader.getInstance()
                        .getMessage(MessagesConstants.EM002, email)));

        final var passwordRecovery = passwordRecoveryRepository
                .findFirstByEmailAndExpiresAfterOrderByExpiresDesc(email,
                        LocalDateTime.now());

        if (passwordRecovery.isPresent()) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                    MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.IM005));
        }

        String token = UUID.randomUUID().toString();
        String resetLink = domainNotificationUrl.concat("?token=").concat(token);
        this.passwordRecoveryRepository
                .save(PasswordRecovery.builder()
                        .token(token)
                        .email(user.getEmail())
                        .expires(LocalDateTime.now().plusMinutes(tokenExpires))
                        .build());
        this.passwordRecoveryPublisher.publish(PasswordRecoveryNotificationDto.builder()
                .urlResetLink(resetLink)
                .recipient(To.builder().name(user.getName()).email(user.getEmail()).build())
                .build());
        logger.info("Password reset initiated for email: {}", email);
        return new ResponseDto<>(HttpStatus.OK.value(),
                MessageLoader.getInstance().getMessage(MessagesConstants.IM002));
    }

    @Override
    public ResponseDto<UserDto> resetPassword(final PasswordRecoveryDto payload) {
        final var passwordRecovery = this.passwordRecoveryRepository.findById(payload.getToken())
                .orElseThrow(() -> new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                        MessagesConstants.EM002, MessageLoader.getInstance()
                        .getMessage(MessagesConstants.EM002, payload.getToken())));

        final var user = this.userRepository.findByEmailIgnoreCase(passwordRecovery.getEmail())
                .orElseThrow(() -> new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                        MessagesConstants.EM002, MessageLoader.getInstance()
                        .getMessage(MessagesConstants.EM002, passwordRecovery.getEmail())));
        final var now = LocalDateTime.now();
        if (now.isAfter(passwordRecovery.getExpires())) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                    MessagesConstants.EM020, MessageLoader.getInstance()
                    .getMessage(MessagesConstants.EM020));
        }
        if (!payload.getPassword().equals(payload.getConfirmPassword())) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                    MessagesConstants.EM021, MessageLoader.getInstance()
                    .getMessage(MessagesConstants.EM021, payload.getToken()));
        }
        user.setPassword(this.passwordEncoder.encode(payload.getPassword()));
        final var userSaved = this.userRepository.save(user);
        return saveAndRespond(userSaved, HttpStatus.OK, MessagesConstants.IM003);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                        MessagesConstants.EM002, MessageLoader.getInstance()
                        .getMessage(MessagesConstants.EM002, username)));
    }

}
