package es.sw.repositorysample.repository.weather;

import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.criteria.StoreCriteria;
import es.sw.repositorysample.repository.exceptions.ObjectCouldNotSavedInRepositoryException;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInRepositoryException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface WeatherRepository {
    Weather findById(long id, FetchCriteria fetchCriteria) throws ObjectNotFoundInRepositoryException;
    boolean save(Weather weather, StoreCriteria storeCriteria) throws ObjectCouldNotSavedInRepositoryException;
}
