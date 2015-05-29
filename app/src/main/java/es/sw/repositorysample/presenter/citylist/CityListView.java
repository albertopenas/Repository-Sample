package es.sw.repositorysample.presenter.citylist;

import java.util.List;

import es.sw.repositorysample.domain.model.City;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface CityListView {
    void cities(List<City> cityList);
}
