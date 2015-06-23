package es.sw.repositorysample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import es.sw.repositorysample.R;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherPressenter;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherView;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;

/**
 * Created by albertopenasamor on 22/6/15.
 */
public class CurrentLocationWeatherFragment extends BaseFragment implements CurrentLocationWeatherView{

    @Inject
    CurrentLocationWeatherPressenter pressenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_current_location_weather, container, false);
        ButterKnife.inject(this, fragmentView);
        return fragmentView;
    }

    @Override
    protected void initialize() {
        //TODO: devuelve componente que no es activity directamente...
        getComponent(CurrentLocationWeatherActivity.class).getComponent().inject(this);
        pressenter.setView(this);
        pressenter.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        pressenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        pressenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pressenter.destroy();
    }
}
