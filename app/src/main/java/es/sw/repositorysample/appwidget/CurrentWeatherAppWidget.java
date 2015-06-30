package es.sw.repositorysample.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import javax.inject.Inject;

import es.sw.repositorysample.di.component.DaggerWeatherAppWidgetComponent;
import es.sw.repositorysample.di.component.WeatherAppWidgetComponent;
import es.sw.repositorysample.di.modules.WeatherAppWidgetModule;

/**
 * Implementation of App Widget functionality.
 */
public class CurrentWeatherAppWidget extends AppWidgetProvider {


    @Inject WeatherAppWidget weatherAppWidget;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = weatherAppWidget.buildAppWidgetsLoading();
        publishAppWidgetUpdate(context, remoteViews);
        context.startService(UpdateWeatherAppWidgetService.newInstance(context));
    }


    @Override
    public void onEnabled(Context context) {}


    @Override
    public void onDisabled(Context context) {}


    @Override
    public void onReceive(Context context, Intent intent) {
        initializeInjections(context);
        super.onReceive(context, intent);
        if (WeatherAppWidget.RELOAD_ACTION.equals(intent.getAction())){
            RemoteViews remoteViews = weatherAppWidget.buildAppWidgetsLoading();
            publishAppWidgetUpdate(context, remoteViews);
            context.startService(UpdateWeatherAppWidgetService.newInstance(context));
        }
    }

    private void initializeInjections(Context context){
        WeatherAppWidgetComponent component = DaggerWeatherAppWidgetComponent.builder()
                .weatherAppWidgetModule(new WeatherAppWidgetModule(context))
                .build();
        component.inject(this);
    }


    public static void publishAppWidgetUpdate(Context context, RemoteViews views){
        ComponentName thisWidget = new ComponentName(context, CurrentWeatherAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }



}