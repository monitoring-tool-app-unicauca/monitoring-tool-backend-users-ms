package co.edu.unicauca.monitoring.tool.backend.users.ms.publisher;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.PasswordRecoveryNotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static co.edu.unicauca.monitoring.tool.backend.users.ms.config.RabbitConfig.PASSWORD_RECOVERY_EXCHANGE;
import static co.edu.unicauca.monitoring.tool.backend.users.ms.config.RabbitConfig.ROUTING_KEY_PASSWORD_RECOVERY;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasswordRecoveryRabbitPublisherImpl implements IPasswordRecoveryPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Async
    @Override
    public void publish(PasswordRecoveryNotificationDto payload) {
        rabbitTemplate.convertAndSend(PASSWORD_RECOVERY_EXCHANGE, ROUTING_KEY_PASSWORD_RECOVERY , payload);
    }
}
