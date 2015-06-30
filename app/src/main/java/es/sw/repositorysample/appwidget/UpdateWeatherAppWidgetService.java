package es.sw.repositorysample.appwidget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.di.component.DaggerWeatherAppWidgetComponent;
import es.sw.repositorysample.di.component.WeatherAppWidgetComponent;
import es.sw.repositorysample.di.modules.WeatherAppWidgetModule;
import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.net.retrofit.WeatherService;
import es.sw.repositorysample.services.LocationService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class UpdateWeatherAppWidgetService extends Service {

    private static final String TAG = CurrentWeatherAppWidget.class.getSimpleName();
    private CompositeSubscription mCompositeSubscription = null;
    private static final int LOCATION_UPDATE_TIMEOUT = 20;

    @Inject WeatherAppWidget weatherAppWidget;


    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, UpdateWeatherAppWidgetService.class);
        return intent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        WeatherAppWidgetComponent component = DaggerWeatherAppWidgetComponent.builder()
                .weatherAppWidgetModule(new WeatherAppWidgetModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG){
            Log.d(TAG, "onStartCommand");
        }
        if (!isRunning()){
            fetchWeatherData();
        }else{
            if (BuildConfig.DEBUG){
                Log.d(TAG, "service already running");
            }
        }
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
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "fetchWeatherData");
        }
        mCompositeSubscription = new CompositeSubscription();

        LocationService locationService = new LocationService(this);
        final Observable locationObservable = locationService.getLocation()
                .timeout(LOCATION_UPDATE_TIMEOUT, TimeUnit.SECONDS)
                .flatMap(new Func1<Location, Observable<CurrentWeather>>() {
                    @Override
                    public Observable<CurrentWeather> call(Location location) {
                        if (BuildConfig.DEBUG) {
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
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "onCompleted");
                        }
                        stopSelf();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "onErrors");
                        }
                        updateAppWidgetsWithError(e);
                        stopSelf();
                    }

                    @Override
                    public void onNext(CurrentWeather currentWeather) {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "onNext");
                        }
                        updateAppWidgets(currentWeather);
                    }
                }));

    }


    private boolean isRunning(){
        return mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed();
    }


    private void updateAppWidgets(CurrentWeather currentWeather){
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "build app widget update");
        }
        RemoteViews updateViews = weatherAppWidget.buildAppWidgetsUpdate(currentWeather);
        CurrentWeatherAppWidget.publishAppWidgetUpdate(UpdateWeatherAppWidgetService.this, updateViews);
    }


    private void updateAppWidgetsWithError(Throwable error){
        RemoteViews updateViews = weatherAppWidget.buildAppWidgetsError(error);
        CurrentWeatherAppWidget.publishAppWidgetUpdate(UpdateWeatherAppWidgetService.this, updateViews);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}