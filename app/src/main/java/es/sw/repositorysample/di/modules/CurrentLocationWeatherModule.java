package es.sw.repositorysample.di.modules;

import dagger.Module;
import dagger.Provides;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherPressenter;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Module
public class CurrentLocationWeatherModule {
    private CurrentLocationWeatherActivity activity;

    public CurrentLocationWeatherModule(CurrentLocationWeatherActivity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    CurrentLocationWeatherActivity provideActivity() {
        return this.activity;
    }

    @Provides
    @PerActivity
    CurrentLocationWeatherPressenter provideCurrentLocationPressenter() {
        return new CurrentLocationWeatherPressenter();
    }
}
