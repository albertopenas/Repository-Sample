package es.sw.repositorysample.appwidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import org.apache.http.HttpException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.R;
import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.helper.TemperatureFormatter;
import es.sw.repositorysample.net.retrofit.WeatherService;
import es.sw.repositorysample.services.LocationService;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;
import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static es.sw.repositorysample.BuildConfig.DEBUG;

/**
 * Implementation of App Widget functionality.
 */
public class CurrentWeatherAppWidget extends AppWidgetProvider {

    private static final String TAG = CurrentWeatherAppWidget.class.getSimpleName();
    private static final String RELOAD_BUTTON_ACTION = "reload_btn";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(UpdateWeatherAppWidgetService.newInstance(context));
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onReceive");
        }
        if (RELOAD_BUTTON_ACTION.equals(intent.getAction())){
            publishAppWidgetUpdate(context, buildAppWidgetsLoading(context));
            context.startService(UpdateWeatherAppWidgetService.newInstance(context));
        }
    }

    public static  void publishAppWidgetUpdate(Context context, RemoteViews views){
        ComponentName thisWidget = new ComponentName(context, CurrentWeatherAppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }


    public static RemoteViews buildAppWidgetsLoading(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_weather_app_widget);

        views.setViewVisibility(R.id.progress_bar, View.VISIBLE);

        views.setViewVisibility(R.id.error_tv, View.GONE);
        views.setViewVisibility(R.id.current_tv, View.GONE);
        views.setViewVisibility(R.id.max_tv, View.GONE);
        views.setViewVisibility(R.id.min_tv, View.GONE);
        views.setViewVisibility(R.id.location_tv, View.GONE);
        views.setViewVisibility(R.id.date_tv, View.GONE);
        views.setViewVisibility(R.id.forecast_tv, View.GONE);
        views.setViewVisibility(R.id.last_updated_at_tv, View.GONE);
        views.setViewVisibility(R.id.thermometer_iv, View.GONE);

        setEvents(context, views);
        return views;
    }

    public static void setEvents(Context context, RemoteViews views){
        views.setOnClickPendingIntent(R.id.widget_ll, makePendingIntent(context));
        views.setOnClickPendingIntent(R.id.reload_btn, getPendingSelfIntent(context, RELOAD_BUTTON_ACTION));
    }

    public static PendingIntent makePendingIntent(Context context){
        Intent intent = new Intent(context, CurrentLocationWeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }

    public static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, CurrentWeatherAppWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }





    public static class UpdateWeatherAppWidgetService extends Service{

        private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        private static final int LOCATION_UPDATE_TIMEOUT = 20;


        public static Intent newInstance(Context context) {
            Intent intent = new Intent(context, UpdateWeatherAppWidgetService.class);
            return intent;
        }


        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (DEBUG){
                Log.d(TAG, "onStartCommand");
            }
            fetchWeatherData();
            return Service.START_STICKY;
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            if (!mCompositeSubscription.isUnsubscribed()) {
                mCompositeSubscription.unsubscribe();
            }
            mCompositeSubscription = null;
        }


        private void fetchWeatherData(){
            if (DEBUG) {
                Log.d(TAG, "fetchWeatherData");
            }
            LocationService locationService = new LocationService(this);
            final Observable locationObservable = locationService.getLocation()
                    .timeout(LOCATION_UPDATE_TIMEOUT, TimeUnit.SECONDS)
                    .flatMap(new Func1<Location, Observable<CurrentWeather>>() {
                        @Override
                        public Observable<CurrentWeather> call(Location location) {
                            if (DEBUG) {
                                Log.d(TAG, "locationService call");
                            }
                            return new WeatherService().fetchCurrentWeather(location.getLongitude(), location.getLatitude())
                                    .map(new Func1<CurrentWeather, CurrentWeather>() {
                                        @Override
                                        public CurrentWeather call(CurrentWeather currentWeather) {
                                            return currentWeather;
                                        }
                                    });
                        }
                    });


            final Handler handler = new Handler();
            mCompositeSubscription.add(locationObservable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.handlerThread(handler))
                    .subscribe(new Subscriber<CurrentWeather>() {
                        @Override
                        public void onCompleted() {
                            if (DEBUG) {
                                Log.d(TAG, "onCompleted");
                            }
                            stopSelf();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (DEBUG) {
                                Log.d(TAG, "onErrors");
                            }
                            updateAppWidgetsWithError(e);
                            stopSelf();
                        }

                        @Override
                        public void onNext(CurrentWeather currentWeather) {
                            if (DEBUG) {
                                Log.d(TAG, "onNext");
                            }
                            updateAppWidgets(currentWeather);
                        }
                    }));

        }


        private void updateAppWidgets(CurrentWeather currentWeather){
            if (DEBUG) {
                Log.d(TAG, "build app widget update");
            }
            RemoteViews updateViews = buildAppWidgetsUpdate(currentWeather);
            publishAppWidgetUpdate(updateViews);
        }


        private void updateAppWidgetsWithError(Throwable error){
            RemoteViews updateViews = buildAppWidgetsError(error);
            publishAppWidgetUpdate(updateViews);
        }


        private RemoteViews buildAppWidgetsError(Throwable error) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.current_weather_app_widget);

            if (error instanceof TimeoutException) {
                views.setTextViewText(R.id.error_tv, getResources().getString(R.string.error_location_unavailable));
            } else if (error instanceof RetrofitError || error instanceof HttpException) {
                views.setTextViewText(R.id.error_tv, getResources().getString(R.string.error_fetch_weather));
            } else {
                Log.e(TAG, error.getMessage());
                error.printStackTrace();
                throw new RuntimeException("See inner exception");
            }


            views.setViewVisibility(R.id.error_tv, View.VISIBLE);
            views.setViewVisibility(R.id.current_tv, View.GONE);
            views.setViewVisibility(R.id.max_tv, View.GONE);
            views.setViewVisibility(R.id.min_tv, View.GONE);
            views.setViewVisibility(R.id.location_tv, View.GONE);
            views.setViewVisibility(R.id.date_tv, View.GONE);
            views.setViewVisibility(R.id.forecast_tv, View.GONE);
            views.setViewVisibility(R.id.last_updated_at_tv, View.GONE);
            views.setViewVisibility(R.id.thermometer_iv, View.GONE);
            views.setViewVisibility(R.id.progress_bar, View.GONE);

            setEvents(views);
            return views;
        }


        private RemoteViews buildAppWidgetsUpdate(CurrentWeather currentWeather) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.current_weather_app_widget);

            views.setTextViewText(R.id.current_tv, TemperatureFormatter.format(currentWeather.getTemperature()));
            views.setTextViewText(R.id.max_tv, TemperatureFormatter.format(currentWeather.getMaximumTemperature()));
            views.setTextViewText(R.id.min_tv, TemperatureFormatter.format(currentWeather.getMinimumTemperature()));

            views.setTextViewText(R.id.location_tv, currentWeather.getLocationName());
            String date = new SimpleDateFormat("EE, d MMM yyyy").format(new Date(currentWeather.getTimestamp()*1000));
            views.setTextViewText(R.id.date_tv, date);
            views.setTextViewText(R.id.forecast_tv, currentWeather.getDescription());

            Date currentDate = new Date(System.currentTimeMillis());
            String currentDateString = new SimpleDateFormat("HH:mm:ss").format(currentDate);
            views.setTextViewText(R.id.last_updated_at_tv, String.format("%s %s", getResources().getString(R.string.last_updated),currentDateString));

            views.setImageViewResource(R.id.thermometer_iv, R.drawable.thermometer);
            views.setViewVisibility(R.id.progress_bar, View.GONE);
            views.setViewVisibility(R.id.error_tv, View.GONE);
            views.setViewVisibility(R.id.current_tv, View.VISIBLE);
            views.setViewVisibility(R.id.max_tv, View.VISIBLE);
            views.setViewVisibility(R.id.min_tv, View.VISIBLE);
            views.setViewVisibility(R.id.location_tv, View.VISIBLE);
            views.setViewVisibility(R.id.date_tv, View.VISIBLE);
            views.setViewVisibility(R.id.forecast_tv, View.VISIBLE);
            views.setViewVisibility(R.id.last_updated_at_tv, View.VISIBLE);
            views.setViewVisibility(R.id.thermometer_iv, View.VISIBLE);

            setEvents(views);
            return views;
        }


        private void setEvents(RemoteViews views){
            views.setOnClickPendingIntent(R.id.widget_ll, makePendingIntent());
            views.setOnClickPendingIntent(R.id.reload_btn, getPendingSelfIntent(RELOAD_BUTTON_ACTION));
        }


        private void publishAppWidgetUpdate(RemoteViews views){
            ComponentName thisWidget = new ComponentName(this, CurrentWeatherAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);
        }


        private PendingIntent makePendingIntent(){
            Intent intent = new Intent(this, CurrentLocationWeatherActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            return pendingIntent;
        }

        protected PendingIntent getPendingSelfIntent(String action) {
            Intent intent = new Intent(this, CurrentWeatherAppWidget.class);
            intent.setAction(action);
            return PendingIntent.getBroadcast(this, 0, intent, 0);
        }


        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}

