package es.sw.repositorysample.repository.weather;

import org.json.JSONException;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.domain.mapper.WeatherCloudMapper;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.domain.server.WeatherCloud;
import es.sw.repositorysample.net.okhttp.webservice.GetWeatherById;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInCloudRepositoryException;
import es.sw.repositorysample.repository.interfaces.DataStore;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Singleton
public class CloudWeatherDataStore implements DataStore<Weather> {

    private GetWeatherById getWeatherById;


    @Inject
    public CloudWeatherDataStore(GetWeatherById getWeatherById) {
        this.getWeatherById = getWeatherById;
    }


    @Override
    public Weather find(long id) throws ObjectNotFoundInCloudRepositoryException {
        return tryFind(id);
    }


    private Weather tryFind(long id) throws ObjectNotFoundInCloudRepositoryException {
        try {
            Weather weather = findInCloud(id);
            if (weather == null) {
                throw new ObjectNotFoundInCloudRepositoryException();
            }
            return weather;
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            throw new ObjectNotFoundInCloudRepositoryException();
        } catch (JSONException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            throw new ObjectNotFoundInCloudRepositoryException();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            throw new ObjectNotFoundInCloudRepositoryException();
        }
    }


    private Weather findInCloud(long id) throws IOException, JSONException, Exception {
        Long longId = Long.valueOf(id);
        WeatherCloud weatherCloud = getWeatherById.start(longId);
        WeatherCloudMapper mapper = new WeatherCloudMapper();
        Weather weather = mapper.map(weatherCloud);
        return weather;
    }


    @Override
    public boolean save(Weather weather) {
        throw new UnsupportedOperationException("CloudWeatherDataStore save method not implemented");
    }
}
