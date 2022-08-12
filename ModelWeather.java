
import java.util.HashMap;


public class ModelWeather {
    private String name;
    public HashMap[] weather;
    public Main main;
    public Wind wind;


    public String getName() {
        return name;
    }
}


class Main {
    private double temp;
    private double humidity;

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }
}

class Wind {
    private double speed;

    public double getSpeed() {
        return speed;
    }
}






