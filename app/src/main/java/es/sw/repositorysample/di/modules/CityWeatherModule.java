package es.sw.repositorysample.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.executor.Executor;
import es.sw.repositorysample.executor.MainThread;
import es.sw.repositorysample.interactor.FetchWeatherInteractor;
import es.sw.repositorysample.interactor.SaveWeatherInteractor;
import es.sw.repositorysample.interactor.UpdateWeatherInteractor;
import es.sw.repositorysample.net.okhttp.OkHttp;
import es.sw.repositorysample.net.okhttp.webservice.GetWeatherById;
import es.sw.repositorysample.presenter.cityweather.CityWeatherPresenter;
import es.sw.repositorysample.repository.weather.CloudWeatherDataStore;
import es.sw.repositorysample.repository.weather.DatabaseWeatherDataStore;
import es.sw.repositorysample.repository.weather.WeatherDataRepository;
import es.sw.repositorysample.repository.weather.WeatherDataStoreFactory;
import es.sw.repositorysample.repository.weather.WeatherRepository;
import es.sw.repositorysample.ui.CityWeatherActivity;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Module
public class CityWeatherModule {
    private CityWeatherActivity activity;

    public CityWeatherModule(CityWeatherActivity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    CityWeatherActivity provideActivity() {
        return this.activity;
    }

    @Provides @PerActivity
    CityWeatherPresenter provideCityWeatherPresenter(FetchWeatherInteractor fetchWeatherInteractor, SaveWeatherInteractor saveWeatherInteractor, UpdateWeatherInteractor updateWeatherInteractor){
        return new CityWeatherPresenter(activity, fetchWeatherInteractor, saveWeatherInteractor, updateWeatherInteractor);
    }

    @Provides @PerActivity
    UpdateWeatherInteractor provideUpdateWeatherInteractor(WeatherRepository weatherRepository, Executor executor, MainThread mainThread){
        return new UpdateWeatherInteractor(weatherRepository, executor, mainThread);
    }

    @Provides @PerActivity
    FetchWeatherInteractor provideFindCityWeatherInteractor(WeatherRepository weatherRepository, Executor executor, MainThread mainThread){
        return new FetchWeatherInteractor(weatherRepository, executor, mainThread);
    }

    @Provides @PerActivity
    SaveWeatherInteractor provideSaveWeatherInteractor(WeatherRepository weatherRepository, Executor executor, MainThread mainThread){
        return new SaveWeatherInteractor(weatherRepository, executor, mainThread);
    }

    @Provides @PerActivity
    WeatherRepository provideWeatherRepository(WeatherDataStoreFactory weatherDataStoreFactory){
        return new WeatherDataRepository(weatherDataStoreFactory);
    }

    @Provides @PerActivity
    WeatherDataStoreFactory provideWeatherDataStoreFactory(CloudWeatherDataStore cloudWeatherDataStore,DatabaseWeatherDataStore databaseWeatherDataStore ){
        return new WeatherDataStoreFactory(cloudWeatherDataStore, databaseWeatherDataStore);
    }

    @Provides @PerActivity
    CloudWeatherDataStore provideServerWeatherDataStore(GetWeatherById getWeatherById){
        return new CloudWeatherDataStore(getWeatherById);
    }

    @Provides @PerActivity
    DatabaseWeatherDataStore provideDatabaseWeatherDataStore(Context context){
        return new DatabaseWeatherDataStore(context);
    }

    @Provides @PerActivity
    GetWeatherById provideGetWeatherById(OkHttp okHttp){
        return new GetWeatherById(okHttp);
    }

}
