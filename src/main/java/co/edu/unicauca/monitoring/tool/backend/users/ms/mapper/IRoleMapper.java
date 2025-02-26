package co.edu.unicauca.monitoring.tool.backend.users.ms.mapper;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.RoleDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRoleMapper extends IEntityMapper<RoleDto, Role> {
}
