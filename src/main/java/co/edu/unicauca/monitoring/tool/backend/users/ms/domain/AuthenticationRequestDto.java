package co.edu.unicauca.monitoring.tool.backend.users.ms.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequestDto {
    private String username;
    private String password;
}
