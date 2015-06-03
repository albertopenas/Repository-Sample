package es.sw.repositorysample.repository.interfaces;

/**
 * Created by AlbertoGarcia on 2/6/15.
 */
public interface Outdate<T> {

    boolean isExpired(T t);

    void setLastUpdate(T t);

}
