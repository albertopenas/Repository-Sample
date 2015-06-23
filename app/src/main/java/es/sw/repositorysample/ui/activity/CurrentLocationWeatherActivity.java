package es.sw.repositorysample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import es.sw.repositorysample.R;
import es.sw.repositorysample.di.HasComponent;
import es.sw.repositorysample.di.component.CurrentLocationWeatherComponent;
import es.sw.repositorysample.di.component.DaggerCurrentLocationWeatherComponent;
import es.sw.repositorysample.di.modules.CurrentLocationWeatherModule;

/**
 * Created by albertopenasamor on 22/6/15.
 */
public class CurrentLocationWeatherActivity extends UIActivity implements HasComponent<CurrentLocationWeatherComponent> {

    private CurrentLocationWeatherComponent component;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, CurrentLocationWeatherActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_city_wheater);
    }

    @Override
    protected void initializeInjector() {
        component = DaggerCurrentLocationWeatherComponent.builder()
                .applicationComponent(getApplicationComponent())
                .currentLocationWeatherModule(new CurrentLocationWeatherModule(this))
                .build();
        component.inject(this);
    }

    @Override
    public CurrentLocationWeatherComponent getComponent() {
        return component;
    }
}
