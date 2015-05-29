package es.sw.repositorysample.domain.mapper;

import es.sw.repositorysample.domain.database.WeatherTable;
import es.sw.repositorysample.domain.model.Weather;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class WeatherTableMapper implements WeatherMapper<WeatherTable>{

    @Override
    public Weather map(WeatherTable weatherTable) {
        Weather weather = new Weather();
        weather.setId(weatherTable.getId());
        weather.setRemoteId(weatherTable.getRemoteId());
        weather.setHumidity(weatherTable.getHumidity());
        weather.setLatitude(weatherTable.getLatitude());
        weather.setLongitude(weatherTable.getLongitude());
        weather.setPressure(weatherTable.getPressure());
        weather.setTemp(weatherTable.getTemp());
        weather.setWindSpeed(weatherTable.getWindSpeed());
        return weather;
    }

    @Override
    public WeatherTable map(Weather weather) {
        WeatherTable weatherTable = new WeatherTable();
        weatherTable.setWindSpeed(weather.getWindSpeed());
        weatherTable.setTemp(weather.getTemp());
        weatherTable.setPressure(weather.getPressure());
        weatherTable.setLongitude(weather.getLongitude());
        weatherTable.setLatitude(weather.getLatitude());
        weatherTable.setHumidity(weather.getHumidity());
        weatherTable.setId(weather.getId());
        weatherTable.setRemoteId(weather.getRemoteId());
        return weatherTable;
    }


}
