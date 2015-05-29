package es.sw.repositorysample.repository.weather;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.criteria.StoreCriteria;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Singleton
public class WeatherDataStoreFactory {

    private CloudWeatherDataStore cloudWeatherDataStore;
    private DatabaseWeatherDataStore databaseWeatherDataStore;

    @Inject
    public WeatherDataStoreFactory(CloudWeatherDataStore cloudWeatherDataStore, DatabaseWeatherDataStore databaseWeatherDataStore) {
        this.cloudWeatherDataStore = cloudWeatherDataStore;
        this.databaseWeatherDataStore = databaseWeatherDataStore;
    }

    public WeatherDataStore get(FetchCriteria fetchCriteria) {
        if (fetchCriteria.name().equals(FetchCriteria.GET.name())){
            return databaseWeatherDataStore;
        }else if(fetchCriteria.name().equals(FetchCriteria.REFRESH.name())){
            return cloudWeatherDataStore;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public WeatherDataStore get(StoreCriteria storeCriteria) {
        if (storeCriteria.name().equals(StoreCriteria.SAVE.name())){
            return databaseWeatherDataStore;
        }else{
            throw new IllegalArgumentException();
        }
    }
}
