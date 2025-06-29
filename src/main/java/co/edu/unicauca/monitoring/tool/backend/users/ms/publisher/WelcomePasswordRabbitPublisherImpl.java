package co.edu.unicauca.monitoring.tool.backend.users.ms.publisher;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.WelcomePasswordNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.config.RabbitConfig.WELCOME_PASSWORD_EXCHANGE;
import static co.edu.unicauca.monitoring.tool.backend.users.ms.config.RabbitConfig.ROUTING_KEY_WELCOME_PASSWORD;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WelcomePasswordRabbitPublisherImpl implements IWelcomePasswordPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Async
    @Override
    public void publish(WelcomePasswordNotificationDto payload) {
        rabbitTemplate.convertAndSend(WELCOME_PASSWORD_EXCHANGE, ROUTING_KEY_WELCOME_PASSWORD, payload);
    }
}
