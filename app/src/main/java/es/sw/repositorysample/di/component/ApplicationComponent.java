/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.sw.repositorysample.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import es.sw.repositorysample.AndroidApp;
import es.sw.repositorysample.di.modules.ApplicationModule;
import es.sw.repositorysample.executor.Executor;
import es.sw.repositorysample.executor.MainThread;
import es.sw.repositorysample.navigation.Navigator;
import es.sw.repositorysample.net.okhttp.OkHttp;
import es.sw.repositorysample.net.util.NetworkUtil;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(AndroidApp androidApp);

  //Exposed to sub-graphs.
  Context provideAppContext();
  Navigator provideNavigator();
  Executor provideThreadExecutor();
  MainThread provideMainThread();
  NetworkUtil provideNetworkUtil();
  OkHttp provideOkHttp();
}
