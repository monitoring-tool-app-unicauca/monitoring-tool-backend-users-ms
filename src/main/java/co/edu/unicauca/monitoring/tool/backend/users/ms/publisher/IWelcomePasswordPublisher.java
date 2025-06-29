package co.edu.unicauca.monitoring.tool.backend.users.ms.publisher;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.WelcomePasswordNotificationDto;

public interface IWelcomePasswordPublisher {
    void publish(final WelcomePasswordNotificationDto payload);
}
