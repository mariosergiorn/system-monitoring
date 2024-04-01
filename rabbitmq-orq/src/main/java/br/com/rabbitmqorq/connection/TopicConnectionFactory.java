package br.com.rabbitmqorq.connection;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConnectionFactory {

    @Bean
    public RabbitAdmin topicRabbitAdmin(CachingConnectionFactory connectionFactory) {

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        TopicExchange exchange = new TopicExchange("topic-exchange");

        admin.declareExchange(exchange);

        Queue securityQueue = new Queue("security-queue");
        Binding securityBinding = BindingBuilder.bind(securityQueue).to(exchange).with("queue.security.*");

        admin.declareQueue(securityQueue);
        admin.declareBinding(securityBinding);

        Queue climateQueue = new Queue("climate-queue");
        Binding climateBinding = BindingBuilder.bind(climateQueue).to(exchange).with("queue.climate.*");

        admin.declareQueue(climateQueue);
        admin.declareBinding(climateBinding);

        Queue eventsQueue = new Queue("events-queue");
        Binding eventsBinding = BindingBuilder.bind(eventsQueue).to(exchange).with("queue.#");

        admin.declareQueue(eventsQueue);
        admin.declareBinding(eventsBinding);

        return admin;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
