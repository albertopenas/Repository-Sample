package es.sw.repositorysample.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import es.sw.repositorysample.domain.model.City;
import es.sw.repositorysample.ui.activity.CityWeatherActivity;
import es.sw.repositorysample.ui.activity.CurrentLocationWeatherActivity;

@Singleton
public class Navigator {

  @Inject
  public void Navigator() {
    //empty
  }

  public void navigateToWeatherCity(Context context, City city) {
      if (city.isCurrentPosition()){
          Intent intent = CurrentLocationWeatherActivity.newIntent(context);
          context.startActivity(intent);
      }else {
          Intent intent = CityWeatherActivity.newIntent(context, city);
          context.startActivity(intent);
      }
  }


}