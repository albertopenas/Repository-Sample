package es.sw.repositorysample.repository.weather;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.exceptions.NoMoreCriteriaException;
import es.sw.repositorysample.repository.exceptions.ObjectCouldNotSavedInRepositoryException;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInCloudRepositoryException;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInDatabaseRepositoryException;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInRepositoryException;
import es.sw.repositorysample.repository.criteria.StoreCriteria;
import es.sw.repositorysample.repository.interfaces.DataStore;
import es.sw.repositorysample.repository.interfaces.Repository;
import es.sw.repositorysample.repository.outdate.WeatherOutdate;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Singleton
public class WeatherDataRepository implements Repository<Weather> {

    private WeatherDataStoreFactory weatherDataStoreFactory;
    private WeatherOutdate weatherOutdate;

    @Inject
    public WeatherDataRepository(WeatherDataStoreFactory weatherDataStoreFactory, WeatherOutdate weatherOutdate) {
        this.weatherDataStoreFactory = weatherDataStoreFactory;
        this.weatherOutdate = weatherOutdate;
    }

    @Override
    public Weather findById(long id, FetchCriteria fetchCriteria) throws ObjectNotFoundInRepositoryException {
        do {
            DataStore weatherDataStore = weatherDataStoreFactory.get(id, fetchCriteria);
            try {
                Weather weather = (Weather) weatherDataStore.find(id);
                if (weather != null) {
                    return weather;
                }
            } catch (ObjectNotFoundInDatabaseRepositoryException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (ObjectNotFoundInCloudRepositoryException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }

            try {
                fetchCriteria.next();
            } catch (NoMoreCriteriaException e) {
                throw new ObjectNotFoundInRepositoryException();
            }
        } while (true);
    }

    @Override
    public boolean save(Weather weather, StoreCriteria storeCriteria) throws ObjectCouldNotSavedInRepositoryException {
        do {
            DataStore weatherDataStore = weatherDataStoreFactory.get(storeCriteria);
            boolean wasSaved = weatherDataStore.save(weather);

            if (!wasSaved) {
                throw new ObjectCouldNotSavedInRepositoryException();
            }

            try {
                storeCriteria = StoreCriteria.next(storeCriteria);
            } catch (NoMoreCriteriaException e) {
                weatherOutdate.setLastUpdate(weather.getRemoteId());
                return wasSaved;
            }
        } while (true);
    }


}
