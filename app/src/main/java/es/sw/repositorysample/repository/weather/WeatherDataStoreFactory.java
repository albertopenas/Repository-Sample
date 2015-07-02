package es.sw.repositorysample.repository.weather;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.criteria.StoreCriteria;
import es.sw.repositorysample.repository.exceptions.NoMoreCriteriaException;
import es.sw.repositorysample.repository.interfaces.DataStore;
import es.sw.repositorysample.repository.outdate.WeatherOutdate;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Singleton
public class WeatherDataStoreFactory {

    private CloudWeatherDataStore cloudWeatherDataStore;
    private DatabaseWeatherDataStore databaseWeatherDataStore;

    private WeatherOutdate weatherOutdate;

    @Inject
    public WeatherDataStoreFactory(CloudWeatherDataStore cloudWeatherDataStore, DatabaseWeatherDataStore databaseWeatherDataStore, WeatherOutdate weatherOutdate) {
        this.cloudWeatherDataStore = cloudWeatherDataStore;
        this.databaseWeatherDataStore = databaseWeatherDataStore;
        this.weatherOutdate = weatherOutdate;
    }

    public DataStore get(long id, FetchCriteria fetchCriteria) {
        if (fetchCriteria.getFetchCriteria().name().equals(FetchCriteria.FetchCriteriaEnum.GET.name())) {
            if (weatherOutdate.isExpired(id)) {
                try {
                    fetchCriteria.next();
                } catch (NoMoreCriteriaException e) {
                    e.printStackTrace();
                }
                return cloudWeatherDataStore;
            }
            return databaseWeatherDataStore;
        } else if (fetchCriteria.getFetchCriteria().name().equals(FetchCriteria.FetchCriteriaEnum.REFRESH.name())) {
            return cloudWeatherDataStore;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public DataStore get(StoreCriteria storeCriteria) {
        if (storeCriteria.name().equals(StoreCriteria.SAVE.name())) {
            return databaseWeatherDataStore;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
