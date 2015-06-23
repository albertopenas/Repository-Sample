package es.sw.repositorysample.services;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import rx.Observable;
import rx.Subscriber;

public class LocationService {

    private final LocationManager mLocationManager;

    public LocationService(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Observable<Location> getLocation() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {

                final LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(final Location location) {
                        subscriber.onNext(location);
                        subscriber.onCompleted();

                        Looper.myLooper().quit();
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        //TODO:
                    }

                    public void onProviderEnabled(String provider) {
                        //TODO:
                    }

                    public void onProviderDisabled(String provider) {
                        //TODO:
                    }
                };

                final Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
                locationCriteria.setPowerRequirement(Criteria.POWER_LOW);
                final String locationProvider = mLocationManager.getBestProvider(locationCriteria, true);

                Looper.prepare();

                mLocationManager.requestSingleUpdate(locationProvider, locationListener, Looper.myLooper());

                Looper.loop();
            }
        });
    }
}