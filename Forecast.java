import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Forecast {
    public static String getForecast(String message, ModelForecast modelForecast) throws IOException {
        URL urlForecast = new URL("https://api.openweathermap.org/data/2.5/forecast?q=" + message + "&units=metric&appid=007c1faa456d100d372b5a7a6dd1fa77");
        Scanner scanner = new Scanner((InputStream) urlForecast.getContent());
        StringBuilder stringBuilderForecast = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilderForecast=stringBuilderForecast.append(scanner.nextLine());
        }
        String resultForecast=stringBuilderForecast.toString();
//      parsing JSON
        Gson gson = new Gson();
        modelForecast = gson.fromJson(resultForecast, ModelForecast.class);

        LinkedHashMap<String, Double> data = new LinkedHashMap<>();

        for (int i = 0; i < modelForecast.list.length; i++) {
            data.put(modelForecast.list[i].getDt_txt(), modelForecast.list[i].main.getTemp());

        }
        StringBuilder sb = new StringBuilder("City: " + modelForecast.city.getName() + "\n");
        for (Map.Entry entry : data.entrySet()) {
            String key = (String) entry.getKey();
            Double value = (Double) entry.getValue();
//            transform dates
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm   dd-MMM  ", Locale.ENGLISH);
            LocalDateTime ldt = LocalDateTime.parse(key, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            sb.append(ldt.format(dtf) + "   " + Math.round(value) + " °C" + "\n");
        }
        return sb.toString();

    }

}


