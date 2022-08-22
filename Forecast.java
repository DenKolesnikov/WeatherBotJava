import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Forecast {

    private final String DATA_TEMPLATE = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATA_TEMPLATE);
    private final String DATE_FOR_USERS = "dd  MMM    HH:mm";
    private final DateTimeFormatter DATE_TIME_FORMATTER_FOR_USERS = DateTimeFormatter.ofPattern(DATE_FOR_USERS, Locale.ENGLISH);
    private final Gson GSON = new Gson();

    public String getForecast(String message) throws IOException {
        ModelForecast forecast = requestForecast(message);
        return "City: " + forecast.city.getName() + "\n"
                + getWeekForecast(forecast);
    }

    private ModelForecast requestForecast(String city) throws IOException {
        URL urlForecast = new URL("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&appid=007c1faa456d100d372b5a7a6dd1fa77");
        Scanner scanner = new Scanner((InputStream) urlForecast.getContent());
        StringBuilder stringBuilderForecast = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilderForecast.append(scanner.nextLine());
        }
        return GSON.fromJson(stringBuilderForecast.toString(), ModelForecast.class);
    }

    private String getWeekForecast(ModelForecast forecasts) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < forecasts.list.length; i++) {
            String date = forecasts.list[i].getDt_txt();
            double temperature = forecasts.list[i].main.getTemp();
            String formattedDay = LocalDateTime.parse(date, DATE_TIME_FORMATTER).format(DATE_TIME_FORMATTER_FOR_USERS);
            sb.append(formattedDay).append("    ").append(Math.round(temperature)).append(" °C").append("\n");
        }
        return sb.toString();
    }
}


