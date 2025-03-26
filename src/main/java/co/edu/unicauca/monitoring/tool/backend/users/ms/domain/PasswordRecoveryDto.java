package co.edu.unicauca.monitoring.tool.backend.users.ms.domain;

import co.edu.unicauca.monitoring.tool.backend.users.ms.rest.common.OnUpdate;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.MessagesConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.util.Constants.PASSWORD_REGULAR_EXPRESSION;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRecoveryDto {
    @NotBlank(message = MessagesConstants.EM008, groups = {OnUpdate.class})
    private String token;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = MessagesConstants.EM008, groups = {OnUpdate.class})
    @Pattern(regexp = PASSWORD_REGULAR_EXPRESSION
            ,groups = {OnUpdate.class}, message = MessagesConstants.EM018)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = MessagesConstants.EM008, groups = {OnUpdate.class})
    @Pattern(regexp = PASSWORD_REGULAR_EXPRESSION
            ,groups = {OnUpdate.class}, message = MessagesConstants.EM018)
    private String confirmPassword;
}
