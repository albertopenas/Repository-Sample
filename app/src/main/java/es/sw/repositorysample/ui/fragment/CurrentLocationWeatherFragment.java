package es.sw.repositorysample.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpException;

import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.sw.repositorysample.R;
import es.sw.repositorysample.di.component.CurrentLocationWeatherComponent;
import es.sw.repositorysample.domain.model.CurrentWeather;
import es.sw.repositorysample.domain.model.WeatherForecast;
import es.sw.repositorysample.helper.TemperatureFormatter;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherPressenter;
import es.sw.repositorysample.presenter.currentlocation.CurrentLocationWeatherView;
import es.sw.repositorysample.ui.interfaces.ActivitySetup;
import es.sw.repositorysample.ui.recycler.ForecastRecyclerViewAdapter;
import retrofit.RetrofitError;

/**
 * Created by albertopenasamor on 22/6/15.
 */
public class CurrentLocationWeatherFragment extends BaseFragment implements CurrentLocationWeatherView, ForecastRecyclerViewAdapter.OnItemClickListener, OnClickListener {

    private static final String TAG = CurrentLocationWeatherFragment.class.getSimpleName();

    private ActivitySetup activitySetup;
    @Inject ForecastRecyclerViewAdapter adapter;
    @Inject CurrentLocationWeatherPressenter pressenter;
    @InjectView(R.id.swipe_refresh_container) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.forecast_recycler) RecyclerView recyclerView;
    @InjectView(R.id.location_name_tv) TextView locationNameTV;
    @InjectView(R.id.current_temperature_tv) TextView temperatureTV;
    @InjectView(R.id.progressBar) ProgressBar progressBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activitySetup = (ActivitySetup) activity;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_current_location_weather, container, false);
        ButterKnife.inject(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSwipeRefresh();
        initRecyclerView();
    }

    @Override
    protected void initialize() {
        getComponent(CurrentLocationWeatherComponent.class).inject(this);
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

    public void setLocationName(String name) {
        if (activitySetup != null){
            activitySetup.setActionBarTitle(name);
        }
    }

    @Override
    public void setCurrentLocationWeather(CurrentWeather currentWeather, List<WeatherForecast> weatherForecastList) {
        setLocationName(currentWeather.getLocationName());
        locationNameTV.setText(currentWeather.getLocationName());
        temperatureTV.setText(TemperatureFormatter.format(currentWeather.getTemperature()));
        adapter.setWeatherForecastList(weatherForecastList);
    }

    @Override
    public void showError(Throwable error) {
        if (error instanceof TimeoutException) {
            Snackbar.make(getView(), getResources().getString(R.string.error_location_unavailable), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.snack_ok), this).show();
        } else if (error instanceof RetrofitError || error instanceof HttpException) {
            Snackbar.make(getView(), getResources().getString(R.string.error_fetch_weather), Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.snack_ok), this).show();
        } else {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
            throw new RuntimeException("See inner exception");
        }
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void alreadyFetchingWeather() {
        swipeRefreshLayout.setRefreshing(false);
        Snackbar.make(getView(), getResources().getString(R.string.already_fetching_weather), Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(View view) {
        Log.d(TAG, "onItemClick");
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), 1, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.accent, R.color.primary, R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pressenter.fetchWeather();
            }
        });
    }

    @Override
    public void onClick(View view) {
        getActivity().finish();
    }
}
