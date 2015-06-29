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
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.R;
import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.helper.TemperatureFormatter;
import es.sw.repositorysample.net.retrofit.WeatherService;
import es.sw.repositorysample.services.LocationService;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;
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
            context.startService(UpdateWeatherAppWidgetService.newInstance(context));
        }
    }

    public static class UpdateWeatherAppWidgetService extends Service{

        private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

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
                    .timeout(30, TimeUnit.SECONDS)
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

            ComponentName thisWidget = new ComponentName(this, CurrentWeatherAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
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

            views.setOnClickPendingIntent(R.id.widget_ll, makePendingIntent());
            views.setOnClickPendingIntent(R.id.reload_btn, getPendingSelfIntent(RELOAD_BUTTON_ACTION));
            return views;
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

