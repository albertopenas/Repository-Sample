package es.sw.repositorysample.interactor;

import java.util.List;

import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.domain.model.WeatherForecast;

/**
 * Created by albertopenasamor on 23/6/15.
 */
public interface FetchForecastForCurrentLocationCallback {

    interface Callback{
        void onNext(final CurrentWeather currentWeather, final List<WeatherForecast> weatherForecastList);
        void onCompleted();
        void onError(final Throwable error);
    }
    void execute(final Callback callback);
}
