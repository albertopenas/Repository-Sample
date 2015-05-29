package es.sw.repositorysample.interactor;

import javax.inject.Inject;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.executor.Executor;
import es.sw.repositorysample.executor.Interactor;
import es.sw.repositorysample.executor.MainThread;
import es.sw.repositorysample.repository.criteria.StoreCriteria;
import es.sw.repositorysample.repository.exceptions.ObjectCouldNotSavedInRepositoryException;
import es.sw.repositorysample.repository.weather.WeatherRepository;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class SaveWeatherInteractor implements Interactor, SaveWeatherCallback {

    private WeatherRepository weatherRepository;
    private Weather weather;
    private StoreCriteria storeCriteria;
    private Callback callback;
    private Executor executor;
    private MainThread mainThread;

    @Inject
    public SaveWeatherInteractor(WeatherRepository weatherRepository, Executor executor, MainThread mainThread){
        this.weatherRepository = weatherRepository;
        this.executor = executor;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        try {
            boolean wasSaved = weatherRepository.save(weather, storeCriteria);
            if (wasSaved) {
                success();
            }else {
                error();
            }
        } catch (ObjectCouldNotSavedInRepositoryException e) {
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
    public void execute(Weather weather, StoreCriteria storeCriteria, Callback callback) {
        this.weather = weather;
        this.storeCriteria= storeCriteria;
        this.callback = callback;
        executor.run(this);
    }

    private void success(){
        if (!isRunning()){
            return;
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.savedWeather();
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
                callback.notSavedWeather();
            }
        });
    }

}
