package ua.thecoon.tgbot.simpletelegrambot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.thecoon.tgbot.simpletelegrambot.config.BotConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final Map<Long, Integer> userMenuState = new ConcurrentHashMap<>();

    private static final int MENU_1 = 1;
    private static final int MENU_2 = 2;

    private static final String BUTTON_1 = "Кнопка 1";
    private static final String BUTTON_2 = "Кнопка 2";
    private static final String NEXT = "Далі";
    private static final String BACK = "Назад";

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        log.debug("Received message: '{}' from chatId: {}", messageText, chatId);

        switch (messageText) {
            case "/start":
                startCommandReceived(chatId);
                break;
            case BUTTON_1:
            case BUTTON_2:
                sendMessage(chatId, messageText);
                break;
            case NEXT:
                showMenu2(chatId);
                break;
            case BACK:
                showMenu1(chatId);
                break;
            default:
                sendMessage(chatId, "Невідома команда");
        }
    }

    private void startCommandReceived(long chatId) {
        log.info("Start command received from chatId: {}", chatId);
        showMenu1(chatId);
    }

    private void showMenu1(long chatId) {
        userMenuState.put(chatId, MENU_1);
        sendMenuMessage(chatId, "Головне меню:", createKeyboard(MENU_1));
    }

    private void showMenu2(long chatId) {
        userMenuState.put(chatId, MENU_2);
        sendMenuMessage(chatId, "Друге меню:", createKeyboard(MENU_2));
    }

    private void sendMessage(Long chatId, String textToSend) {
        int menuState = userMenuState.getOrDefault(chatId, MENU_1);
        sendMenuMessage(chatId, textToSend, createKeyboard(menuState));
    }

    private void sendMenuMessage(long chatId, String text, List<KeyboardRow> keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
            log.debug("Message sent to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Error sending message to chatId: {}", chatId, e);
        }
    }

    private List<KeyboardRow> createKeyboard(int menuState) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(BUTTON_1);
        keyboard.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(BUTTON_2);
        keyboard.add(row2);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(menuState == MENU_1 ? NEXT : BACK);
        keyboard.add(row3);

        return keyboard;
    }
}
