package es.sw.repositorysample.interactor;

import javax.inject.Inject;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.executor.Executor;
import es.sw.repositorysample.executor.Interactor;
import es.sw.repositorysample.executor.MainThread;
import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInRepositoryException;
import es.sw.repositorysample.repository.weather.WeatherRepository;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class UpdateWeatherInteractor implements Interactor, UpdateWeatherCallback {

    private WeatherRepository weatherRepository;
    private Weather weather;
    private FetchCriteria fetchCriteria;
    private Callback callback;
    private Executor executor;
    private MainThread mainThread;

    @Inject
    public UpdateWeatherInteractor(WeatherRepository weatherRepository, Executor executor, MainThread mainThread){
        this.weatherRepository = weatherRepository;
        this.executor = executor;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        try {
            long remoteId = weather.getRemoteId();
            Weather newWeather = weatherRepository.findById(remoteId, fetchCriteria);
            newWeather.setId(weather.getId());
            success(newWeather);
        } catch (ObjectNotFoundInRepositoryException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            error();
        }
    }

    @Override
    public boolean isRunning() {
        return callback != null;
    }

    @Override
    public void cancel() {
        callback = null;
    }

    @Override
    public void execute(Weather weather, FetchCriteria fetchCriteria, Callback callback) {
        this.weather = weather;
        this.fetchCriteria = fetchCriteria;
        this.callback = callback;
        executor.run(this);
    }

    private void success(final Weather weather){
        if (!isRunning()){
            return;
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.foundWeather(weather);
            }
        });
    }

    private void error(){
        if (!isRunning()){
            return;
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.notFoundWeather();
            }
        });
    }

}
