package co.edu.unicauca.monitoring.tool.backend.users.ms.mapper;


import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.UserDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper extends IEntityMapper<UserDto, User>{

    User toDomain(UserDto dto);
    UserDto toDto(User entity);

}
