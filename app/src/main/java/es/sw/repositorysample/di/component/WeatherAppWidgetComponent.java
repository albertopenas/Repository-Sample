package es.sw.repositorysample.di.component;

import javax.inject.Singleton;

import dagger.Component;
import es.sw.repositorysample.appwidget.CurrentWeatherAppWidget;
import es.sw.repositorysample.appwidget.UpdateWeatherAppWidgetService;
import es.sw.repositorysample.appwidget.WeatherAppWidget;
import es.sw.repositorysample.di.modules.WeatherAppWidgetModule;

/**
 * Created by albertopenasamor on 30/6/15.
 */
@Singleton
@Component(modules = WeatherAppWidgetModule.class)
public interface WeatherAppWidgetComponent {
    void inject(UpdateWeatherAppWidgetService updateWeatherAppWidgetService);
    void inject(CurrentWeatherAppWidget currentWeatherAppWidget);

    WeatherAppWidget provideWeatherAppWidget();
}
