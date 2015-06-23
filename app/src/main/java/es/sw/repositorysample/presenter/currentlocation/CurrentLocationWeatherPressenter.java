package es.sw.repositorysample.presenter.currentlocation;

import android.util.Log;

import java.util.List;

import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.domain.model.WeatherForecast;
import es.sw.repositorysample.interactor.FetchForecastForCurrentLocationCallback;
import es.sw.repositorysample.interactor.FetchForecastForCurrentLocationInteractor;
import es.sw.repositorysample.presenter.Presenter;

/**
 * Created by albertopenasamor on 23/6/15.
 */
public class CurrentLocationWeatherPressenter implements Presenter{

    private static final String TAG = CurrentLocationWeatherPressenter.class.getSimpleName();
    private CurrentLocationWeatherView view;
    private FetchForecastForCurrentLocationInteractor fetchForecastForCurrentLocationInteractor;

    public CurrentLocationWeatherPressenter(FetchForecastForCurrentLocationInteractor fetchForecastForCurrentLocationInteractor) {
        this.fetchForecastForCurrentLocationInteractor = fetchForecastForCurrentLocationInteractor;
    }
    
    @Override
    public void create() {
        Log.d(TAG, "create");
        fetchWeather();
        view.showLoading();
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
        fetchForecastForCurrentLocationInteractor.cancel();
    }

    public void setView(CurrentLocationWeatherView view) {
        this.view = view;
    }

    public void fetchWeather(){
        fetchForecastForCurrentLocationInteractor.execute(new FetchForecastForCurrentLocationCallback.Callback() {
            @Override
            public void onNext(final CurrentWeather currentWeather, final List<WeatherForecast> weatherForecastList) {
                view.setCurrentLocationWeather(currentWeather, weatherForecastList);
            }

            @Override
            public void onCompleted() {
                view.hideLoading();
            }

            @Override
            public void onError(final Throwable error) {
                view.hideLoading();
                view.showError(error);
            }
        });
    }
}
