package es.sw.repositorysample;

import android.app.Application;
import android.util.Log;

import es.sw.repositorysample.di.component.ApplicationComponent;
import es.sw.repositorysample.di.component.DaggerApplicationComponent;
import es.sw.repositorysample.di.modules.ApplicationModule;
import es.sw.repositorysample.net.volley.VolleySingleton;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class AndroidApp extends Application {

    private static final String TAG = AndroidApp.class.getSimpleName();
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        super.onCreate();
        initializeInjector();
        VolleySingleton.getInstance(getApplicationContext());
    }


    private void initializeInjector() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "initializeInjector");
        }
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
