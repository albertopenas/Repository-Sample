package es.sw.repositorysample.di.component;

import dagger.Component;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.di.modules.CityWeatherModule;
import es.sw.repositorysample.presenter.cityweather.CityWeatherPresenter;
import es.sw.repositorysample.ui.CityWeatherActivity;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = CityWeatherModule.class)
public interface CityWeatherComponent {

    void inject(CityWeatherActivity activity);

    CityWeatherPresenter provideCityWeatherPresenter();
}
