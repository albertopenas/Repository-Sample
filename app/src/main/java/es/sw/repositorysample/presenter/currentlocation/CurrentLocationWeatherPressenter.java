package es.sw.repositorysample.presenter.currentlocation;

import android.util.Log;

import es.sw.repositorysample.presenter.Presenter;

/**
 * Created by albertopenasamor on 23/6/15.
 */
public class CurrentLocationWeatherPressenter implements Presenter{

    private static final String TAG = CurrentLocationWeatherPressenter.class.getSimpleName();
    private CurrentLocationWeatherView view;

    public CurrentLocationWeatherPressenter() {}

    public void setView(CurrentLocationWeatherView view) {
        this.view = view;
    }

    @Override
    public void create() {
        Log.d(TAG, "create");
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
    }
}
