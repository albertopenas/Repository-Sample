package es.sw.repositorysample.presenter.cityweather;

import android.util.Log;

import javax.inject.Inject;

import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.domain.model.City;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.interactor.FetchWeatherCallback;
import es.sw.repositorysample.interactor.FetchWeatherInteractor;
import es.sw.repositorysample.interactor.SaveWeatherCallback;
import es.sw.repositorysample.interactor.SaveWeatherInteractor;
import es.sw.repositorysample.interactor.UpdateWeatherCallback;
import es.sw.repositorysample.interactor.UpdateWeatherInteractor;
import es.sw.repositorysample.presenter.Presenter;
import es.sw.repositorysample.repository.criteria.FetchCriteria;
import es.sw.repositorysample.repository.criteria.StoreCriteria;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@PerActivity
public class CityWeatherPresenter implements Presenter {

    private static final String TAG = CityWeatherPresenter.class.getSimpleName();
    private CityWeatherView view;
    private FetchWeatherInteractor fetchWeatherInteractor;
    private SaveWeatherInteractor saveWeatherInteractor;
    private UpdateWeatherInteractor updateWeatherInteractor;

    @Inject
    public CityWeatherPresenter(CityWeatherView cityWeatherView, FetchWeatherInteractor fetchWeatherInteractor, SaveWeatherInteractor saveWeatherInteractor, UpdateWeatherInteractor updateWeatherInteractor){
        this.view = cityWeatherView;
        this.fetchWeatherInteractor = fetchWeatherInteractor;
        this.saveWeatherInteractor = saveWeatherInteractor;
        this.updateWeatherInteractor = updateWeatherInteractor;
    }

    @Override
    public void create() {
        Log.d(TAG, "create");
        view.prepare();
        view.setupUi();
    }

    @Override
    public void resume() {
        Log.d(TAG, "resume");
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy");
        if (fetchWeatherInteractor.isRunning()){
            fetchWeatherInteractor.cancel();
        }
        if (saveWeatherInteractor.isRunning()){
            saveWeatherInteractor.cancel();
        }
    }

    public void findWeatherForCity(City city){
        Log.d(TAG, "findWeatherForCity");
        fetchWeatherInteractor.execute(city.getId(), FetchCriteria.GET, new FetchWeatherCallback.Callback() {
            @Override
            public void foundWeather(Weather weather) {
                view.weather(weather);
                saveWeatherForCity(weather);
            }

            @Override
            public void notFoundWeather() {
                view.weatherError();
            }
        });
    }

    public void refreshWeatherForCity(Weather weather){
        Log.d(TAG, "findWeatherForCity");
        updateWeatherInteractor.execute(weather, FetchCriteria.REFRESH, new UpdateWeatherCallback.Callback() {
            @Override
            public void foundWeather(Weather weather) {
                view.weather(weather);
                saveWeatherForCity(weather);
            }

            @Override
            public void notFoundWeather() {
                view.weatherError();
            }
        });
    }

    private void saveWeatherForCity(Weather weather){
        Log.d(TAG, "findWeatherForCity");
        saveWeatherInteractor.execute(weather, StoreCriteria.SAVE, new SaveWeatherCallback.Callback() {
            @Override
            public void savedWeather() {
                Log.d(TAG, "savedWeather");
            }

            @Override
            public void notSavedWeather() {
                Log.d(TAG, "notSavedWeather");
            }
        });
    }
}
