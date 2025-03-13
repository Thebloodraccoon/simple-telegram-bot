package ua.thecoon.tgbot.simpletelegrambot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ua.thecoon.tgbot.simpletelegrambot.service.TelegramBot;


@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {
    private final TelegramBot telegramBot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            log.info("Telegram bot {} successfully registered", telegramBot.getBotUsername());

            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e){
            log.error("Failed to register bot: {}", e.getMessage(), e);
        }
    }
}
