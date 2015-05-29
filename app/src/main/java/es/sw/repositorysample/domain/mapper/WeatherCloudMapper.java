package es.sw.repositorysample.domain.mapper;

import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.domain.server.WeatherCloud;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class WeatherCloudMapper implements WeatherMapper<WeatherCloud> {

    public Weather map(WeatherCloud weatherCloud) {
        Weather weather = new Weather();
        weather.setRemoteId(weatherCloud.getRemoteId());
        weather.setHumidity(weatherCloud.getHumidity());
        weather.setLatitude(weatherCloud.getLatitude());
        weather.setLongitude(weatherCloud.getLongitude());
        weather.setPressure(weatherCloud.getPressure());
        weather.setTemp(weatherCloud.getTemp());
        weather.setWindSpeed(weatherCloud.getWindSpeed());
        return weather;
    }

    @Override
    public WeatherCloud map(Weather weather) {
        throw new UnsupportedOperationException("WeatherCloudMapper operation not implemented");
    }
}
