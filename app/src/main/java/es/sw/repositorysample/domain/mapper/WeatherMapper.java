package es.sw.repositorysample.domain.mapper;

import es.sw.repositorysample.domain.model.Weather;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface WeatherMapper<T> {
    Weather map(T t);
    T map(Weather weather);
}
