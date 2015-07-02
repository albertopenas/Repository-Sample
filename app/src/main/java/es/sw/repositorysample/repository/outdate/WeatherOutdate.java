package es.sw.repositorysample.repository.outdate;

import android.content.Context;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.Period;

import es.sw.repositorysample.repository.interfaces.Outdate;

/**
 * Created by AlbertoGarcia on 2/6/15.
 */
public class WeatherOutdate implements Outdate<Long> {

    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String KEY_WEATHER_ID = "key_weather_id:";
    private int minutesBeforeExpire;
    private Context context;

    public WeatherOutdate(Context context, int minutesBeforeExpire) {
        this.context = context;
        this.minutesBeforeExpire = minutesBeforeExpire;
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    private SharedPreferences getShared() {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    private String getWeatherDate(Long id) {
        return getShared().getString(KEY_WEATHER_ID + id, "");
    }

    @Override
    public boolean isExpired(Long id) {

        String weatherDate = getWeatherDate(id);
        if (weatherDate.isEmpty()) {
            return false;
        }
        //TODO: se quedó frita alguna vez en debug aquí, revisar si puede haber algun problema con los strings o fue el maldito debug
        DateTime previousDateTime = new DateTime(weatherDate);//2015-07-02T15:32:53.249+02:00
        DateTime currentDate = DateTime.now();

        Period diff = new Period(previousDateTime, currentDate);
        int diffMin = diff.getMinutes();

        if (diffMin > minutesBeforeExpire) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setLastUpdate(Long id) {
        getEditor().putString(KEY_WEATHER_ID + id, DateTime.now().toString()).commit();
    }
}
