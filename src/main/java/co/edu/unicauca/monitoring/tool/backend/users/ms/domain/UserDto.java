package co.edu.unicauca.monitoring.tool.backend.users.ms.domain;

import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnCreate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnUpdate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long userId;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String documentNumber;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String firstName;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String secondName;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String firstLastName;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String secondLastName;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private Long phoneNumber;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String email;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$"
        ,groups = {OnCreate.class}, message = MessagesConstants.EM008)
    private String password;
//    private List<RoleDto> roles;

}
