package br.com.thermostat.controller;


import br.com.thermostat.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/publish")
public class RequestController {

    @Value("${thermostat-service.exchange}")
    public String exchange;

    @Value("${thermostat-service.routingKey.info}")
    public String routingKeyInfo;

    @Value("${thermostat-service.routingKey.temperature}")
    public String routingKeyTemperature;

    private final RabbitTemplate rabbitTemplate;

    public RequestController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/message/info")
    public ResponseEntity<HttpStatus> postMessageInfo(@RequestBody Message message) {

        log.info("Sending message to exchange {} with routingKey {}", exchange, routingKeyInfo);
        rabbitTemplate.convertAndSend(exchange, routingKeyInfo, message);
        log.info("Message published successfully");

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping(value = "/message/temperature")
    public ResponseEntity<HttpStatus> postMessageTemperature(@RequestBody Message message) {

        log.info("Sending message to exchange {} with routingKey {}", exchange, routingKeyTemperature);
        rabbitTemplate.convertAndSend(exchange, routingKeyTemperature, message);
        log.info("Message published successfully");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
