package es.sw.repositorysample.repository.interfaces;

import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInCloudRepositoryException;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInDatabaseRepositoryException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface DataStore<T> {
    T find(long id) throws ObjectNotFoundInCloudRepositoryException, ObjectNotFoundInDatabaseRepositoryException;

    boolean save(T weather);
}
