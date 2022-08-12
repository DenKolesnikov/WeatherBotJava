import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


public class Weather {


    //007c1faa456d100d372b5a7a6dd1fa77 API
    public static String getWeather(String message, ModelWeather modelWeather) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=007c1faa456d100d372b5a7a6dd1fa77");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder stringBuilderWeather = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilderWeather=stringBuilderWeather.append(scanner.nextLine());
        }
        String result = stringBuilderWeather.toString();
//      parsing JSON
        Gson gson = new Gson();
        modelWeather = gson.fromJson(result, ModelWeather.class);

        return
                "City: " + modelWeather.getName() + "\n" +
                        "Temperature : " + Math.round(modelWeather.main.getTemp()) + "°C" + "\n" +
                        "Humidity: " + Math.round(modelWeather.main.getHumidity()) + " % " + "\n" +
                        "Conditions: " + modelWeather.weather[0].get("main") + " / " + modelWeather.weather[0].get("description") + "\n" +
                        "Wind: " + modelWeather.wind.getSpeed() + " m/s ";


    }
}
