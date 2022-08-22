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
    private final Weather CURRENT_WEATHER = new Weather();
    private final Forecast WEEK_FORECAST = new Forecast();


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
                } else setInlineKeyboard(sendMessage);

            }

        } else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            CallbackQuery callback = update.getCallbackQuery();
            String data = callback.getData();
            SendMessage sendMessageCallback = new SendMessage();

            if (data.contains("current")) {
                sendMessageCallback.setChatId(chatId);
                try {
                    sendMessageCallback.setText(CURRENT_WEATHER.getWeather(data.replace("current", "")));
                } catch (IOException e) {
                    sendMessageCallback.setText("The city is not found!");
                }
            } else if (data.contains("forecast")) {

                sendMessageCallback.setChatId(chatId);
                try {
                    sendMessageCallback.setText(WEEK_FORECAST.getForecast(data.replace("forecast", "")));
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
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        KeyboardRow startButton = new KeyboardRow();
        List<KeyboardRow> keyboardStart = new ArrayList<>();

        sendButton.setReplyMarkup(replyKeyboard);
        replyKeyboard.setSelective(true);
        replyKeyboard.setResizeKeyboard(true);
        replyKeyboard.setOneTimeKeyboard(false);

        startButton.add(new KeyboardButton("/start"));
        keyboardStart.add(startButton);
        replyKeyboard.setKeyboard(keyboardStart);
        sendButton.setReplyMarkup(replyKeyboard);
        sendButton.setText("Please, enter a city:");
        sendButton.setChatId(message.getChatId());
        try {
            execute(sendButton);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setInlineKeyboard(SendMessage sendInline) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        InlineKeyboardButton currentWeatherButton = new InlineKeyboardButton();
        InlineKeyboardButton forecastButton = new InlineKeyboardButton();
        List<InlineKeyboardButton> currentWeatherRow = new ArrayList<>();
        List<InlineKeyboardButton> forecastRow = new ArrayList<>();
        List<List<InlineKeyboardButton>> buttonsRow = new ArrayList<>();

        sendInline.setText(message.getText());
        sendInline.setChatId(message.getChatId());

        currentWeatherButton.setText("Current weather");
        currentWeatherButton.setCallbackData(message.getText() + "current");

        forecastButton.setText("5-day forecast");
        forecastButton.setCallbackData(message.getText() + "forecast");

        currentWeatherRow.add(currentWeatherButton);
        forecastRow.add(forecastButton);
        buttonsRow.add(currentWeatherRow);
        buttonsRow.add(forecastRow);
        inlineKeyboard.setKeyboard(buttonsRow);
        sendInline.setReplyMarkup(inlineKeyboard);
        try {
            execute(sendInline);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}










