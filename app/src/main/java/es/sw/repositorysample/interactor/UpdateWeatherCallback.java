package es.sw.repositorysample.interactor;

import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.criteria.FetchCriteria;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface UpdateWeatherCallback {

    interface Callback{
        void foundWeather(Weather weather);
        void notFoundWeather();
    }

    void execute(Weather weather, FetchCriteria fetchCriteria, Callback callback);
}
