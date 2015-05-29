package es.sw.repositorysample.presenter.citylist;

import android.util.Log;

import javax.inject.Inject;

import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.domain.dummy.DummyCities;
import es.sw.repositorysample.presenter.Presenter;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@PerActivity
public class CityListPresenter implements Presenter {

    private static final String TAG = CityListPresenter.class.getSimpleName();
    private CityListView view;

    @Inject
    public CityListPresenter(CityListView cityListView){
        this.view = cityListView;
    }


    @Override
    public void create() {
        Log.d(TAG, "create");
    }

    @Override
    public void resume() {
        Log.d(TAG, "resume");
        view.cities(DummyCities.cityList);
    }

    @Override
    public void pause() {
        Log.d(TAG, "pause");
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy");
    }

    public void showCity(){
        Log.d(TAG, "showCity");
    }
}
