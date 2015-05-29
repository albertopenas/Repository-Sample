package es.sw.repositorysample.repository.weather;

import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInCloudRepositoryException;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInDatabaseRepositoryException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface WeatherDataStore {
    Weather find(long id) throws ObjectNotFoundInCloudRepositoryException, ObjectNotFoundInDatabaseRepositoryException;
    boolean save(Weather weather);
}
