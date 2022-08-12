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
        ModelWeather modelWeather = new ModelWeather();
        ModelForecast modelForecast = new ModelForecast();
        Message message = update.getMessage();

        if (update.hasMessage()) {

            if (message.hasText()) {
                String text = message.getText();
                SendMessage sendMessage = new SendMessage();
//                add "/start" keyboard
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                replyKeyboardMarkup.setSelective(true);
                replyKeyboardMarkup.setResizeKeyboard(true);
                replyKeyboardMarkup.setOneTimeKeyboard(false);

                List<KeyboardRow> keyboardStart = new ArrayList<>();
                KeyboardRow startButton = new KeyboardRow();
                startButton.add(new KeyboardButton("/start"));
                keyboardStart.add(startButton);
                replyKeyboardMarkup.setKeyboard(keyboardStart);

                sendMessage.setReplyMarkup(replyKeyboardMarkup);
                if (text.equals("/start")) {
                    sendMessage.setText("Enter a city, please:");
                    sendMessage.setChatId(message.getChatId());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {

//                   add InlineButtons and CallBack
                    sendMessage.setText(message.getText());
                    sendMessage.setChatId(message.getChatId());
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
                    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                    try {
                        execute(sendMessage);

                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (update.hasCallbackQuery()) {
//            get CallBack
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            SendMessage sendMessage = new SendMessage();
            if (data.contains("current")) {
//              sending current weather
                sendMessage.setChatId(chatId);
                try {
                    sendMessage.setText(Weather.getWeather(data.replace("current", ""), modelWeather));
                } catch (IOException e) {
                    sendMessage.setText("The city is not found!");
                }

            } else if (data.contains("forecast")) {
//                sending forecast
                sendMessage.setChatId(chatId);
                try {
                    sendMessage.setText(Forecast.getForecast(data.replace("forecast", ""), modelForecast));
                } catch (IOException e) {
                    sendMessage.setText("The city is not found!");
                }
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

}










