package br.com.security.config.controller;

import br.com.security.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/publish")
public class RequestController {

    @Value("${security-services.exchange}")
    public String exchange;

    @Value("${security-services.routingKey}")
    public String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public RequestController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/message/info")
    public ResponseEntity<HttpStatus> postMessage(@RequestBody(required=false) Message message) {

        log.info("Sending message to exchange {} with routingKey {}", exchange, routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        log.info("Message published successfully");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
