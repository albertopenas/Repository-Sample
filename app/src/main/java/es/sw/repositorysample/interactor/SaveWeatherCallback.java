package es.sw.repositorysample.interactor;

import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.criteria.StoreCriteria;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface SaveWeatherCallback {

    interface Callback{
        void savedWeather();
        void notSavedWeather();
    }

    void execute(Weather weather, StoreCriteria storeCriteria, Callback callback);
}
