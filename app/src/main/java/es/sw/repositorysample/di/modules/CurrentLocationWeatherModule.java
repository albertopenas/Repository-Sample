package es.sw.repositorysample.di.modules;

import dagger.Module;
import dagger.Provides;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.helper.DayFormatter;
import es.sw.repositorysample.interactor.FetchForecastForCurrentLocationInteractor;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherPressenter;
import es.sw.repositorysample.services.LocationService;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;
import es.sw.repositorysample.ui.recycler.ForecastRecyclerViewAdapter;

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
    CurrentLocationWeatherPressenter provideCurrentLocationPressenter(FetchForecastForCurrentLocationInteractor fetchForecastForCurrentLocationInteractor) {
        return new CurrentLocationWeatherPressenter(fetchForecastForCurrentLocationInteractor);
    }

    @Provides
    @PerActivity
    ForecastRecyclerViewAdapter provideForecastRecyclerViewAdapter(DayFormatter dayFormatter){
        return new ForecastRecyclerViewAdapter(dayFormatter);
    }

    @Provides
    @PerActivity
    DayFormatter provideDayFormatter(){
        return new DayFormatter(activity);
    }


    @Provides
    FetchForecastForCurrentLocationInteractor provideFetchForecastForCurrentLocationInteractor(LocationService locationService){
        return new FetchForecastForCurrentLocationInteractor(locationService);
    }

    @Provides
    @PerActivity
    LocationService provideLocationService(){
        return new LocationService(activity);
    }
}
