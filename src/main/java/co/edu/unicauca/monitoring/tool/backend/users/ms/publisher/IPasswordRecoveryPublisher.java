package co.edu.unicauca.monitoring.tool.backend.users.ms.publisher;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.PasswordRecoveryNotificationDto;

public interface IPasswordRecoveryPublisher {
    void publish(final PasswordRecoveryNotificationDto payload);
}
