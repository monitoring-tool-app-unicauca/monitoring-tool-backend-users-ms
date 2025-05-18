package co.edu.unicauca.monitoring.tool.backend.users.ms.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PASSWORD_RECOVERY_QUEUE = "password.recovery.queue";
    public static final String ROUTING_KEY_PASSWORD_RECOVERY = "password.recovery";
    public static final String PASSWORD_RECOVERY_EXCHANGE = "password.recovery.exchange";

    public static final String WELCOME_PASSWORD_QUEUE = "welcome.password.queue";
    public static final String ROUTING_KEY_WELCOME_PASSWORD= "welcome.password";
    public static final String WELCOME_PASSWORD_EXCHANGE = "welcome.password.exchange";

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange passwordRecoveryExchange() {
        return new TopicExchange(PASSWORD_RECOVERY_EXCHANGE);
    }

    @Bean
    public Binding bindingCreated() {
        return BindingBuilder.bind(passwordRecoveryQueue())
                .to(passwordRecoveryExchange())
                .with(ROUTING_KEY_PASSWORD_RECOVERY);
    }

    @Bean
    public Queue passwordRecoveryQueue() {
        return new Queue(PASSWORD_RECOVERY_QUEUE, true);
    }

    @Bean
    public TopicExchange welcomePasswordExchange() {
        return new TopicExchange(WELCOME_PASSWORD_EXCHANGE);
    }

    @Bean
    public Queue welcomePasswordQueue() {
        return new Queue(WELCOME_PASSWORD_QUEUE, true);
    }

    @Bean
    public Binding bindingWelcomePassword() {
        return BindingBuilder.bind(welcomePasswordQueue())
                .to(welcomePasswordExchange())
                .with(ROUTING_KEY_WELCOME_PASSWORD);
    }

}
