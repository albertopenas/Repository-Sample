package es.sw.repositorysample.interactor;

import android.location.Location;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.domain.model.WeatherForecast;
import es.sw.repositorysample.executor.Interactor;
import es.sw.repositorysample.net.retrofit.WeatherService;
import es.sw.repositorysample.services.LocationService;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by albertopenasamor on 23/6/15.
 */
public class FetchForecastForCurrentLocationInteractor implements Interactor, FetchForecastForCurrentLocationCallback {

    private static final String KEY_CURRENT_WEATHER = "key_current_weather";
    private static final String KEY_WEATHER_FORECASTS = "key_weather_forecasts";
    private static final long LOCATION_TIMEOUT_SECONDS = 20;

    private boolean isRunning = false;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    private Callback callback;
    final LocationService locationService;

    public FetchForecastForCurrentLocationInteractor(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public void execute(final Callback callback) {
        this.callback = callback;
        run();
    }

    @Override
    public void run() {
        // Get our current location.
        isRunning = true;

        final Observable fetchDataObservable = locationService.getLocation()
                .timeout(LOCATION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .flatMap(new Func1<Location, Observable<HashMap<String, WeatherForecast>>>() {
                    @Override
                    public Observable<HashMap<String, WeatherForecast>> call(final Location location) {
                        final WeatherService weatherService = new WeatherService();
                        final double longitude = location.getLongitude();
                        final double latitude = location.getLatitude();

                        return Observable.zip(
                                // Fetch current and 7 day forecasts for the location.
                                weatherService.fetchCurrentWeather(longitude, latitude),
                                weatherService.fetchWeatherForecasts(longitude, latitude),

                                // Only handle the fetched results when both sets are available.
                                new Func2<CurrentWeather, List<WeatherForecast>, HashMap<String, WeatherForecast>>() {
                                    @Override
                                    public HashMap call(final CurrentWeather currentWeather, final List<WeatherForecast> weatherForecasts) {

                                        HashMap weatherData = new HashMap();
                                        weatherData.put(KEY_CURRENT_WEATHER, currentWeather);
                                        weatherData.put(KEY_WEATHER_FORECASTS, weatherForecasts);
                                        return weatherData;
                                    }
                                }
                        );
                    }
                });

        mCompositeSubscription.add(fetchDataObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<HashMap<String, WeatherForecast>>() {
                            @Override
                            public void onNext(final HashMap<String, WeatherForecast> weatherData) {
                                // current weather.
                                final CurrentWeather currentWeather = (CurrentWeather) weatherData.get(KEY_CURRENT_WEATHER);
                                // weather forecast list.
                                final List<WeatherForecast> weatherForecasts = (List<WeatherForecast>) weatherData.get(KEY_WEATHER_FORECASTS);
                                callback.onNext(currentWeather, weatherForecasts);
                            }

                            @Override
                            public void onCompleted() {
                                isRunning = false;
                                callback.onCompleted();
                            }

                            @Override
                            public void onError(final Throwable error) {
                                isRunning = false;
                                callback.onError(error);
                            }
                        })
        );
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void cancel() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()){
            mCompositeSubscription.unsubscribe();
        }
        isRunning = false;
    }
}
