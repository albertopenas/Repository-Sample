package es.sw.repositorysample.repository.weather;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.domain.database.WeatherTable;
import es.sw.repositorysample.domain.mapper.WeatherTableMapper;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInDatabaseRepositoryException;
import es.sw.repositorysample.repository.interfaces.DataStore;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Singleton
public class DatabaseWeatherDataStore implements DataStore<Weather> {

    private WeakReference<Context> contextWeakReference;


    @Inject
    public DatabaseWeatherDataStore(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }


    @Override
    public Weather find(long id) throws ObjectNotFoundInDatabaseRepositoryException {
        return tryFind(id);
    }


    private Weather tryFind(long id) throws ObjectNotFoundInDatabaseRepositoryException {

        try {
            Weather weather = findInDatabase(id);
            if (weather == null) {
                throw new ObjectNotFoundInDatabaseRepositoryException();
            }
            return weather;
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            throw new ObjectNotFoundInDatabaseRepositoryException();
        }
    }


    private Weather findInDatabase(long id) throws Exception {
        List<WeatherTable> weatherTableList = WeatherTable.findByField(contextWeakReference.get(), WeatherTable.class, WeatherTable.REMOTE_ID, Long.valueOf(id));
        WeatherTable weatherTable = weatherTableList.get(0);
        WeatherTableMapper mapper = new WeatherTableMapper();
        Weather weather = mapper.map(weatherTable);
        return weather;
    }


    @Override
    public boolean save(Weather weather) {
        WeatherTableMapper mapper = new WeatherTableMapper();
        WeatherTable weatherTable = mapper.map(weather);

        boolean wasSaved = WeatherTable.save(contextWeakReference.get(), weatherTable);
        return wasSaved;
    }
}
