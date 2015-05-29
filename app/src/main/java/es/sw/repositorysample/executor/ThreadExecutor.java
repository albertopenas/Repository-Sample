/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.sw.repositorysample.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ThreadExecutor implements Executor {

  private static final int CORE_POOL_SIZE = 1;
  private static final int MAX_POOL_SIZE = 1;
  private static final int KEEP_ALIVE_TIME = 120;
  private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
  private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue<>();

  private ThreadPoolExecutor threadPoolExecutor;

  @Inject
  public ThreadExecutor() {
    int corePoolSize = CORE_POOL_SIZE;
    int maxPoolSize = MAX_POOL_SIZE;
    int keepAliveTime = KEEP_ALIVE_TIME;
    TimeUnit timeUnit = TIME_UNIT;
    BlockingQueue<Runnable> workQueue = WORK_QUEUE;
    threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, workQueue);
  }

  @Override
  public void run(final Interactor interactor) {
    if (interactor == null) {
      throw new IllegalArgumentException("Interactor to execute can't be null");
    }
    threadPoolExecutor.submit(new Runnable() {
      @Override public void run() {
        interactor.run();
      }
    });
  }
}
