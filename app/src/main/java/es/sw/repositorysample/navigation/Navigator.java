package es.sw.repositorysample.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.domain.model.City;
import es.sw.repositorysample.ui.CityWeatherActivity;

@Singleton
public class Navigator {

  @Inject
  public void Navigator() {
    //empty
  }

  public void navigateToWeatherCity(Context context, City city) {
      Intent intent = CityWeatherActivity.newIntent(context, city);
      context.startActivity(intent);
  }


}