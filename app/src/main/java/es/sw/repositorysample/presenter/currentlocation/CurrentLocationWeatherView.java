package es.sw.repositorysample.presenter.currentlocation;

import java.util.List;

import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.domain.model.WeatherForecast;

/**
 * Created by albertopenasamor on 23/6/15.
 */
public interface CurrentLocationWeatherView {
    void setCurrentLocationWeather(final CurrentWeather currentWeather, final List<WeatherForecast> weatherForecastList);
    void showError(final Throwable error);
    void showLoading();
    void hideLoading();
    void alreadyFetchingWeather();
}
