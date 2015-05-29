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
package es.sw.repositorysample.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.sw.repositorysample.AndroidApp;
import es.sw.repositorysample.executor.Executor;
import es.sw.repositorysample.executor.MainThread;
import es.sw.repositorysample.executor.MainThreadImpl;
import es.sw.repositorysample.executor.ThreadExecutor;
import es.sw.repositorysample.navigation.Navigator;
import es.sw.repositorysample.net.okhttp.OkHttp;
import es.sw.repositorysample.net.util.NetworkUtil;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

    private final AndroidApp application;

    public ApplicationModule(AndroidApp application) {
        this.application = application;
    }

    @Provides @Singleton Context provideApplicationContext() {
        return this.application;
    }

    @Provides @Singleton
    NetworkUtil provideNetworkUtil() {
        return new NetworkUtil(application);
    }

    //TODO: no usable desde preenters!!!!, no tiene el contexto...(NECESITA CONTEXTO DE ACTIVITY:...)
    @Provides @Singleton
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides @Singleton
    Executor provideThreadExecutor(ThreadExecutor executor) {
        return executor;
    }

    @Provides @Singleton
    MainThread providePostExecutionThread(MainThreadImpl mainThread) {
        return mainThread;
    }

    @Provides @Singleton
    OkHttp provideOkHttp(){
        return new OkHttp();
    }
}
