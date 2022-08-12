import java.util.HashMap;

public class ModelForecast {

    public List[] list;
    public City city;
}

class City {

    private String name;

    public String getName() {
        return name;
    }
}

class List {

    public MainForecast main;
    private HashMap[] weather;

    private String dt_txt;

    public MainForecast getMain() {
        return main;
    }

    public HashMap[] getWeather() {
        return weather;
    }


    public String getDt_txt() {
        return dt_txt;
    }
}


class MainForecast {
    private double temp;


    public double getTemp() {
        return temp;
    }

}



