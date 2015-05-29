package es.sw.repositorysample.net.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.apache.http.impl.client.DefaultHttpClient;

import es.sw.repositorysample.net.httpclient.ApacheHttpClientStack;


public class VolleySingleton {

    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;
 
    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context, new ApacheHttpClientStack(new DefaultHttpClient()));
    }

    /**
     * First time init(Should be called in Application class before use it)
     * @param context
     * @return
     */
    public static VolleySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    /**
     * Get volley singleton
     * @return
     */
    public static VolleySingleton getInstance(){
        return mInstance;
    }
 
    public RequestQueue getRequestQueue(){
        return this.mRequestQueue;
    }
}