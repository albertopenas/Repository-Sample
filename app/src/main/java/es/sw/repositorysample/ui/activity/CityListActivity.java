package es.sw.repositorysample.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.R;
import es.sw.repositorysample.ui.adapter.CityAdapter;
import es.sw.repositorysample.di.component.CityListComponent;
import es.sw.repositorysample.di.component.DaggerCityListComponent;
import es.sw.repositorysample.di.modules.CityListActivityModule;
import es.sw.repositorysample.domain.model.City;
import es.sw.repositorysample.navigation.Navigator;
import es.sw.repositorysample.presenter.citylist.CityListPresenter;
import es.sw.repositorysample.presenter.citylist.CityListView;


public class CityListActivity extends BaseActivity implements CityListView{

    private static final String TAG = CityListActivity.class.getSimpleName();


    private CityAdapter adapter;
    @InjectView(R.id.listview) ListView listView;
    @Inject CityListPresenter presenter;
    @Inject Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.inject(this);
        uiInit();
    }


    @Override
    protected void initializeInjector() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "initializeInjector");
        }
        CityListComponent component = DaggerCityListComponent.builder()
                .applicationComponent(getApplicationComponent())
                .cityListActivityModule(new CityListActivityModule(this))
                .build();
        component.inject(this);
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
        ButterKnife.reset(this);
        presenter.destroy();
    }

    @OnItemClick(R.id.listview)
    public void onItemSelected(int position) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onItemSelected");
        }
        navigator.navigateToWeatherCity(this, adapter.getItem(position));
    }


    @Override
    public void cities(List<City> cityList) {
        adapter.setCityList(cityList);
    }


    private void uiInit() {
        adapter = new CityAdapter(this);
        listView.setAdapter(adapter);
    }
}

