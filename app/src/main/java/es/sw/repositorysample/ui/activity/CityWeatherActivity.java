package es.sw.repositorysample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.sw.repositorysample.R;
import es.sw.repositorysample.di.component.CityWeatherComponent;
import es.sw.repositorysample.di.component.DaggerCityWeatherComponent;
import es.sw.repositorysample.di.modules.CityWeatherModule;
import es.sw.repositorysample.domain.dummy.DummyCities;
import es.sw.repositorysample.domain.model.City;
import es.sw.repositorysample.domain.model.Weather;
import es.sw.repositorysample.presenter.cityweather.CityWeatherPresenter;
import es.sw.repositorysample.presenter.cityweather.CityWeatherView;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class CityWeatherActivity extends UIActivity implements CityWeatherView{

    private static final String TAG = CityWeatherActivity.class.getSimpleName();
    private City city;
    private Weather weather;
    @InjectView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.lat) TextView latitudeTV;
    @InjectView(R.id.longitude) TextView longitudeTV;
    @InjectView(R.id.temp) TextView temperatureTV;
    @InjectView(R.id.pressure) TextView pressureTV;
    @InjectView(R.id.humidity) TextView humidityTV;
    @InjectView(R.id.windspeed) TextView windSpeedTV;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.progress_bar) ProgressBar progressBar;
    @InjectView(R.id.toolbar_title) TextView toolbarTitleTV;
    @Inject CityWeatherPresenter presenter;

    public static Intent newIntent(Context context, City city){
        Intent intent = new Intent(context, CityWeatherActivity.class);
        intent.putExtra("params", city.getId());
        return intent;
    }


    private City getParams(){
        long cityId = getIntent().getLongExtra("params", -1);
        City city = null;
        try {
            city = DummyCities.findById(cityId);
        } catch (Exception e) {
            e.printStackTrace();
            city = new City(-1, "");
        }
        return city;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_wheater);
        ButterKnife.inject(this);

        presenter.create();
        presenter.findWeatherForCity(city);
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        presenter.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        ButterKnife.reset(this);
    }


    @Override
    protected void initializeInjector() {
        CityWeatherComponent component = DaggerCityWeatherComponent.builder()
                .applicationComponent(getApplicationComponent())
                .cityWeatherModule(new CityWeatherModule(this))
                .build();
        component.inject(this);
    }


    @Override
    public void weather(Weather weather) {
        Log.e(TAG, "weather");
        swipeRefreshLayout.setEnabled(true);
        this.weather = weather;

        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

        latitudeTV.setText(String.valueOf(weather.getLatitude()));
        longitudeTV.setText(String.valueOf(weather.getLongitude()));
        temperatureTV.setText(String.valueOf(weather.getTemp()));
        pressureTV.setText(String.valueOf(weather.getPressure()));
        humidityTV.setText(String.valueOf(weather.getHumidity()));
        windSpeedTV.setText(String.valueOf(weather.getWindSpeed()));
    }


    @Override
    public void weatherError() {
        Log.e(TAG, "NOT finded weather");
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        toast(getResources().getString(R.string.error_loading_weather));
    }


    @Override
    public void prepare() {
        swipeRefreshLayout.setEnabled(false);
        city = getParams();
    }


    @Override
    public void setupUi() {
        setSupportActionBar(toolbar);
        toolbarTitleTV.setText(city.getName());
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_light, R.color.primary, R.color.primary_dark, R.color.secondary_text_disabled_material_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshWeatherForCity(weather);
            }
        });

    }

}
