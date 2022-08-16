import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WeatherBot extends TelegramLongPollingBot {
    private Message message;

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new WeatherBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return "testweathermoscowbot";
    }

    @Override
    public String getBotToken() {
        return "5382782041:AAGPoPpiyCIRVYf7hT8ctiLNLCV1jC2Yj58";
    }

    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();

        if (update.hasMessage()) {
            SendMessage sendMessage = new SendMessage();
            if (message.hasText()) {
                String text = message.getText();

                if (text.equals("/start")) {
                    setKeyboardButton(sendMessage);
                }
                else setInlineKeyboard(sendMessage);

            }

        } else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            SendMessage sendMessageCallback = new SendMessage();
            if (data.contains("current")) {
                sendMessageCallback.setChatId(chatId);
                try {
                    sendMessageCallback.setText(Weather.getWeather(data.replace("current", "")));
                } catch (IOException e) {
                    sendMessageCallback.setText("The city is not found!");
                }

            } else if (data.contains("forecast")) {
                sendMessageCallback.setChatId(chatId);
                try {
                    sendMessageCallback.setText(Forecast.getForecast(data.replace("forecast", "")));
                } catch (IOException e) {
                    sendMessageCallback.setText("The city is not found!");
                }
            }
            try {
                execute(sendMessageCallback);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }


    private void setKeyboardButton(SendMessage sendButton) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendButton.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardStart = new ArrayList<>();
        KeyboardRow startButton = new KeyboardRow();
        startButton.add(new KeyboardButton("/start"));
        keyboardStart.add(startButton);
        replyKeyboardMarkup.setKeyboard(keyboardStart);
        sendButton.setReplyMarkup(replyKeyboardMarkup);
        sendButton.setText("Enter a city, please:");
        sendButton.setChatId(message.getChatId());
        try {
            execute(sendButton);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setInlineKeyboard(SendMessage sendInline) {
        sendInline.setText(message.getText());
        sendInline.setChatId(message.getChatId());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton currentButton = new InlineKeyboardButton();
        currentButton.setText("Current weather");
        currentButton.setCallbackData(message.getText() + "current");
        InlineKeyboardButton forecastButton = new InlineKeyboardButton();
        forecastButton.setText("5-day forecast");
        forecastButton.setCallbackData(message.getText() + "forecast");
        List<InlineKeyboardButton> buttonsUp = new ArrayList<>();
        List<InlineKeyboardButton> buttonsDown = new ArrayList<>();
        buttonsUp.add(currentButton);
        buttonsDown.add(forecastButton);
        List<List<InlineKeyboardButton>> row = new ArrayList<>();
        row.add(buttonsUp);
        row.add(buttonsDown);
        inlineKeyboardMarkup.setKeyboard(row);
        sendInline.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendInline);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}










