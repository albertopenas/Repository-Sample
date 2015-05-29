package es.sw.repositorysample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import es.sw.repositorysample.AndroidApp;
import es.sw.repositorysample.BuildConfig;
import es.sw.repositorysample.di.component.ApplicationComponent;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

  private static final String TAG = BaseActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "onCreate");
    }
    super.onCreate(savedInstanceState);
    initializeInjector();
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApp)getApplication()).getApplicationComponent();
  }

  protected abstract void initializeInjector();

}
