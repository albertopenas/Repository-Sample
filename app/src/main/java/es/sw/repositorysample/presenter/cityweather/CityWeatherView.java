package es.sw.repositorysample.presenter.cityweather;

import es.sw.repositorysample.domain.model.Weather;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface CityWeatherView {
    void prepare();
    void setupUi();
    void weather(Weather weather);
    void weatherError();
}
