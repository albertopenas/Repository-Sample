package es.sw.repositorysample.net.okhttp;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@Singleton
public class OkHttp {

    private OkHttpClient client;

    @Inject
    public OkHttp() {
        client = new OkHttpClient();
    }

    public Response execute(Request request) throws IOException, IllegalStateException {
        return client.newCall(request).execute();
    }

    /**
     * Cancels all petitions tagget with tag param
     * @param tag
     */
    public void cancelAll(String tag){
        client.cancel(tag);
    }


}
