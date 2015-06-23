package es.sw.repositorysample.di.component;

import dagger.Component;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.di.modules.CurrentLocationWeatherModule;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherPressenter;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;
import es.sw.repositorysample.ui.fragment.CurrentLocationWeatherFragment;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = CurrentLocationWeatherModule.class)
public interface CurrentLocationWeatherComponent {

    void inject(CurrentLocationWeatherActivity activity);
    void inject(CurrentLocationWeatherFragment fragment);

    CurrentLocationWeatherPressenter provideCurrentLocationWeatherPressenter();
}
