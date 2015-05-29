package es.sw.repositorysample.repository.exceptions;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class ObjectNotFoundInDatabaseRepositoryException extends Exception {

    public ObjectNotFoundInDatabaseRepositoryException() {
        super("Object not found in database repository");
    }

}
