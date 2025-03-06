package co.edu.unicauca.monitoring.tool.backend.users.ms.business;

import co.edu.unicauca.monitoring.tool.backend.users.ms.config.MessageLoader;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.ResponseDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.RoleDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.exception.BusinessRuleException;
import co.edu.unicauca.monitoring.tool.backend.users.ms.mapper.IRoleMapper;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.Role;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IRoleRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__ (@Autowired))
public class RoleBusinessImpl implements IRoleBusiness {

  private static final Logger logger = LoggerFactory.getLogger(UserBusinessImpl.class);

  private final IRoleRepository roleRepository;
  private final IRoleMapper roleMapper;

  @Override
  public ResponseDto<RoleDto> createRole(RoleDto payload) {
    validateRoleNameUniqueness(payload.getName());

    Role role = this.roleMapper.toDomain(payload);
    Role roleSaved = roleRepository.save(role);
    logger.info("Role has been created!");
    return new ResponseDto<>(HttpStatus.CREATED.value(),
        MessageLoader.getInstance().getMessage(MessagesConstants.IM002),
        roleMapper.toDto(roleSaved));
  }

  @Override
  public ResponseDto<RoleDto> updateRole(Long id, RoleDto payload) {
    Role role = this.roleRepository.findById(id)
        .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
            MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, id)));

    if (!payload.getName().equalsIgnoreCase(role.getName())) {
      validateRoleNameUniqueness(payload.getName());
    }

    role.setDescription(payload.getDescription());
    role.setName(payload.getName());

    Role roleSaved = this.roleRepository.save(role);
    logger.info("Role has been updated!");
    return new ResponseDto<>(HttpStatus.OK.value(),
        MessageLoader.getInstance().getMessage(MessagesConstants.IM003),
        roleMapper.toDto(roleSaved));
  }

  private void validateRoleNameUniqueness(String roleName) {
    if (roleRepository.findByNameIgnoreCase(roleName).isPresent()) {
      throw new BusinessRuleException(HttpStatus.BAD_REQUEST.value(),
          MessagesConstants.EM010,
          MessageLoader.getInstance().getMessage(MessagesConstants.EM010, roleName));
    }
  }


  @Override
  public ResponseDto<RoleDto> getRoleById(Long id) {
    Role role = this.roleRepository.findById(id)
        .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
            MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, id)));
    logger.info("Role has been found!");
    return new ResponseDto<>(HttpStatus.OK.value(),
        MessageLoader.getInstance().getMessage(MessagesConstants.IM001),
        roleMapper.toDto(role));
  }



  @Override
  public ResponseDto<Void> deleteRole(Long id) {
    Role role = this.roleRepository.findById(id)
        .orElseThrow(() -> new BusinessRuleException(HttpStatus.OK.value(),
            MessagesConstants.EM002, MessageLoader.getInstance().getMessage(MessagesConstants.EM002, id)));

    this.roleRepository.delete(role);
    logger.info("Role has been deleted!");

    return new ResponseDto<>(HttpStatus.OK.value(),
        MessageLoader.getInstance().getMessage(MessagesConstants.IM003));
  }

  @Override
  public ResponseDto<List<RoleDto>> getAllRoles() {
    List<RoleDto> usersResponse = roleRepository.findAll().stream()
        .map(roleMapper::toDto)
        .toList();
    logger.info("All users have been fetched!");
    return new ResponseDto<>(HttpStatus.OK.value(),
        MessageLoader.getInstance().getMessage(MessagesConstants.IM001), usersResponse);
  }
}
