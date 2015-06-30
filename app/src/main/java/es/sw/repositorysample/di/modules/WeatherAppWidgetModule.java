package es.sw.repositorysample.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.sw.repositorysample.appwidget.WeatherAppWidget;

/**
 * Created by albertopenasamor on 30/6/15.
 */
@Module
public class WeatherAppWidgetModule {


    private Context context;


    public WeatherAppWidgetModule(Context context) {
        this.context = context;
    }


    @Provides
    @Singleton
    WeatherAppWidget provideWeatherAppWidget(){
        return new WeatherAppWidget(context);
    }
}
