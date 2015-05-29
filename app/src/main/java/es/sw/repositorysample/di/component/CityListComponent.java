package es.sw.repositorysample.di.component;

import dagger.Component;
import es.sw.repositorysample.di.PerActivity;
import es.sw.repositorysample.di.modules.CityListActivityModule;
import es.sw.repositorysample.presenter.citylist.CityListPresenter;
import es.sw.repositorysample.ui.CityListActivity;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = CityListActivityModule.class)
public interface CityListComponent {
    void inject(CityListActivity activity);

    CityListActivity provideActivity();
    CityListPresenter provideCityListPresenter();
}
