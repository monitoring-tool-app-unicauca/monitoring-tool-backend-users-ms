package co.edu.unicauca.monitoring.tool.backend.users.ms.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WelcomePasswordNotificationDto {
    private String generatedPassword;
    private To recipient;
}
