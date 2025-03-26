package co.edu.unicauca.monitoring.tool.backend.users.ms.domain;

import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnCreate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnUpdate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.util.Constants.PASSWORD_REGULAR_EXPRESSION;

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
    private String name;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private String lastName;
    @NotNull(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    private Long phoneNumber;
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class, OnUpdate.class})
    @Email(message = MessagesConstants.EM018, groups = {OnCreate.class})
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = MessagesConstants.EM008, groups = {OnCreate.class})
    @Pattern(regexp = PASSWORD_REGULAR_EXPRESSION
        ,groups = {OnCreate.class}, message = MessagesConstants.EM018)
    private String password;
    private List<Long> roleIds;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<RoleDto> roles;
}
