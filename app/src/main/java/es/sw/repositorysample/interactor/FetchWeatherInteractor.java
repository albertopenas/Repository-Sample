package es.sw.repositorysample.interactor;

import javax.inject.Inject;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.executor.Executor;
import es.sw.repositorysample.executor.Interactor;
import es.sw.repositorysample.executor.MainThread;
import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.exceptions.ObjectNotFoundInRepositoryException;
import es.sw.repositorysample.repository.interfaces.Repository;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class FetchWeatherInteractor implements Interactor, FetchWeatherCallback {

    private Repository weatherRepository;
    private long id;
    private FetchCriteria fetchCriteria;
    private Callback callback;
    private Executor executor;
    private MainThread mainThread;

    @Inject
    public FetchWeatherInteractor(Repository weatherRepository, Executor executor, MainThread mainThread) {
        this.weatherRepository = weatherRepository;
        this.executor = executor;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        try {
            Weather weather = (Weather) weatherRepository.findById(id, fetchCriteria);
            success(weather);
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
    public void execute(long id, FetchCriteria fetchCriteria, Callback callback) {
        this.id = id;
        this.fetchCriteria = fetchCriteria;
        this.callback = callback;
        executor.run(this);
    }

    private void success(final Weather weather) {
        if (!isRunning()) {
            return;
        }

        mainThread.post(new Runnable() {
            @Override
            public void run() {
                callback.foundWeather(weather, fetchCriteria);
            }
        });
    }

    private void error() {
        if (!isRunning()) {
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
