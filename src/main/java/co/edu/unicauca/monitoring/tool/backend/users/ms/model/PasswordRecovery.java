package co.edu.unicauca.monitoring.tool.backend.users.ms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PasswordRecovery {

    @Id
    private String token;
    private LocalDateTime expires;
    private String email;
}
