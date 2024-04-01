package br.com.climate.consumer;

import br.com.climate.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClimateConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${climate.queue}")
    private String queue;

    @RabbitListener(queues = "${climate.queue}")
    public void receiveMessage(String jsonMessage) throws JsonProcessingException {

        Message output = objectMapper.readValue(jsonMessage, Message.class);

        log.info("Event recevied from {}: {}", queue, output.toString());

    }
}
