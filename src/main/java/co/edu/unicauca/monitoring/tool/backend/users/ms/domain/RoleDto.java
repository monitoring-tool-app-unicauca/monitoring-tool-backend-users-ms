package co.edu.unicauca.monitoring.tool.backend.users.ms.domain;

import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnCreate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnUpdate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
    private Long roleId;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String description;
}
