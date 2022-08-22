import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


public class Weather {

    private final Gson GSON = new Gson();

    public String getWeather(String message) throws IOException {
        ModelWeather weather = requestCurrentWeather(message);
        return weatherConditions(weather);
    }

    private ModelWeather requestCurrentWeather(String city) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=007c1faa456d100d372b5a7a6dd1fa77");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder stringBuilderWeather = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilderWeather.append(scanner.nextLine());
        }

        return GSON.fromJson(stringBuilderWeather.toString(), ModelWeather.class);
    }

    private String weatherConditions(ModelWeather conditions) {
        return "City : " + conditions.getName() + "\n" +
                "Temperature : " + Math.round(conditions.main.getTemp()) + "°C" + "\n" +
                "Humidity : " + Math.round(conditions.main.getHumidity()) + " % " + "\n" +
                "Description : " + conditions.weather[0].get("main") + " / " + conditions.weather[0].get("description") + "\n" +
                "Wind : " + conditions.wind.getSpeed() + " m/s ";
    }

}

