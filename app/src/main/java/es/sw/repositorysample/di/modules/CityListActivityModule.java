package es.sw.repositorysample.di.modules;

import dagger.Module;
import dagger.Provides;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.presenter.citylist.CityListPresenter;
import es.sw.repositorysample.ui.activity.CityListActivity;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Module
public class CityListActivityModule  {

    private final CityListActivity activity;

    public CityListActivityModule(CityListActivity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides @PerActivity
    CityListActivity provideActivity() {
        return this.activity;
    }

    @Provides @PerActivity
    CityListPresenter provideCityListPresenter(){
        return new CityListPresenter(activity);
    }
}
