package es.sw.repositorysample.repository.exceptions;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class ObjectNotFoundInRepositoryException extends Exception {

    public ObjectNotFoundInRepositoryException() {
        super("Object not found in any of repositories");
    }

}
