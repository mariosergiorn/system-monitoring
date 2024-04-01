package br.com.rabbitmqorq.controller;

import br.com.rabbitmqorq.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/resources")
public class RequestController {

    private final RabbitTemplate rabbitTemplate;

    public RequestController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping(value = "/message")
    public ResponseEntity<HttpStatus> postMessage(@RequestParam("exchange") String exchange,
                                      @RequestParam("routingKey") String routingKey, @RequestBody Message message) {

        log.info("Sending message to exchange {} with routingKey {}", exchange, routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
