package ru.petrelevich.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.petrelevich.domain.Message;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private static final String TOPIC_TEMPLATE = "/topic/response.";

    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;

    public MessageController(WebClient datastoreClient, SimpMessagingTemplate template) {
        this.datastoreClient = datastoreClient;
        this.template = template;
    }

    @MessageMapping("/message.{roomId}")
    public void getMessage(@DestinationVariable("roomId") String roomId, Message message) {
        if ("1408".equals(roomId)) {
            throw new ChatException("Нельзя отправлять сообщения в комнату 1408.");
        }

        saveMessage(roomId, message).subscribe(msgId -> logger.info("Message saved with ID: {}", msgId));
        template.convertAndSend(
                String.format("%s%s", TOPIC_TEMPLATE, roomId),
                new Message(HtmlUtils.htmlEscape(message.messageStr()))
        );

        // Отправка сообщения в комнату 1408
        saveMessage("1408", message)
                .subscribe(msgId -> logger.info("Message также отправлено в комнату 1408 с ID: {}", msgId));
        template.convertAndSend(
                String.format("%s%s", TOPIC_TEMPLATE, "1408"),
                new Message(HtmlUtils.htmlEscape(message.messageStr()))
        );
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            logger.error("Не удалось получить заголовок simpDestination, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Не удалось получить заголовок simpDestination");
        }

        if (!simpDestination.startsWith(template.getUserDestinationPrefix())) {
            return;
        }

        var roomId = parseRoomId(simpDestination);
        logger.info("Подписка на:{}, roomId:{}", simpDestination, roomId);

        if ("1408".equals(roomId)) {
            logger.info("Пользователь подписался на комнату 1408");
            getAllMessages()
                    .doOnError(ex -> logger.error("Ошибка загрузки сообщений для комнаты 1408", ex))
                    .subscribe(message -> template.convertAndSend(simpDestination, message));
        } else {
            getMessagesByRoomId(roomId)
                    .doOnError(ex -> logger.error("Ошибка загрузки сообщений для комнаты: {}", roomId, ex))
                    .subscribe(message -> template.convertAndSend(simpDestination, message));
        }
    }

    private Flux<Message> getAllMessages() {
        return datastoreClient
                .get()
                .uri("/msg/all")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Message.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }

    private long parseRoomId(String simpDestination) {
        try {
            var idxRoom = simpDestination.lastIndexOf(TOPIC_TEMPLATE);
            return Long.parseLong(simpDestination.substring(idxRoom).replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            logger.error("Can not get roomId", ex);
            throw new ChatException("Can not get roomId");
        }
    }

    private Mono<Long> saveMessage(String roomId, Message message) {
        return datastoreClient
                .post()
                .uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<Message> getMessagesByRoomId(long roomId) {
        return datastoreClient
                .get()
                .uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Message.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }
}
