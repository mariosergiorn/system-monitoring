package br.com.security.consumer;

import br.com.security.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${security.queue}")
    private String queue;

    @RabbitListener(queues = "${security.queue}")
    public void receiveMessage(String jsonMessage) throws JsonProcessingException {

        Message output = objectMapper.readValue(jsonMessage, Message.class);

        log.info("Event recevied from {}: {}", queue, output.toString());

    }
}
