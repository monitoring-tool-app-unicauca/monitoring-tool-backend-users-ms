package co.edu.unicauca.monitoring.tool.backend.users.ms.business;


import co.edu.unicauca.monitoring.tool.backend.users.ms.config.MessageLoader;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.exception.BusinessRuleException;
import co.edu.unicauca.monitoring.tool.backend.users.ms.mapper.IUserMapper;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IUserRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__ (@Autowired))
public class UserBusinessImpl implements IUserBusiness {
    private static final Logger logger = LoggerFactory.getLogger(UserBusinessImpl.class);

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    public ResponseDto<UserDto> createUser(UserDto userDto) {

        User user = this.userMapper.toDomain(userDto);
        User userSaved = userRepository.save(user);
        logger.info("User has been created!");
        return new ResponseDto<>(HttpStatus.CREATED.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001, userDto.getDocumentNumber()),
            userMapper.toDto(userSaved));
    }

    @Override
    public ResponseDto<UserDto> getUserById(Long userId) {

        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                MessagesConstants.EM001, MessageLoader.getInstance().getMessage(MessagesConstants.EM001, userId)));
        logger.info("User has been found!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001, userId),
            userMapper.toDto(user));
    }

    @Override
    public ResponseDto<UserDto> updateUser(Long id, UserDto userDto) {
        if (userDto == null || StringUtils.isEmpty(userDto.getDocumentNumber())) {
            throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
                MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, "userDto"));
        }

        User user = this.userRepository.findById(id)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                MessagesConstants.EM001, MessageLoader.getInstance().getMessage(MessagesConstants.EM001, id.toString())));

        user.setFirstName(userDto.getFirstName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setSecondName(userDto.getSecondName());
        this.userRepository.save(user);
        logger.info("User has been updated!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001, userDto.getDocumentNumber()),
            userMapper.toDto(user));
    }

    @Override
    public ResponseDto<Void> deleteUser(Long userId) {

        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new BusinessRuleException(HttpStatus.NOT_FOUND.value(),
                MessagesConstants.EM001, MessageLoader.getInstance().getMessage(MessagesConstants.EM001, userId)));

        userRepository.delete(user);
        logger.info("User has been deleted!");

        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001, userId));
    }

    @Override
    public ResponseDto<List<UserDto>> getAllUsers() {
        List<UserDto> usersResponse = userRepository.findAll().stream()
            .map(userMapper::toDto)
                .toList();
        logger.info("All users have been fetched!");
        return new ResponseDto<>(HttpStatus.OK.value(),
            MessageLoader.getInstance().getMessage(MessagesConstants.IM001, "users"), usersResponse);
    }
}
