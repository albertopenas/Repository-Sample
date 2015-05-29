package es.sw.repositorysample.net.volley.jsonarray;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import java.util.Map;

import es.sw.repositorysample.net.volley.ResponseListener;


/**
 * Created by ivancarreira on 25/03/15.
 */
public abstract class GetJsonArrayRequest<T,J> extends JsonArrayRequest<T, J> {
    private static final String TAG = GetJsonArrayRequest.class.getSimpleName();
    private static final int DEFAULT_GET_REQUESTS_TIMEOUT = 12000;
    private static final int MAX_NUM_RETRIES = 1;

    /**
     * Make a request and return a parsed object from JSON.
     *
     * @param url
     * @param headers
     * @param responseListener
     * @param errorListener
     */
    public GetJsonArrayRequest(String url, Map<String, String> headers, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, headers, responseListener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_GET_REQUESTS_TIMEOUT, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public GetJsonArrayRequest(String url, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, null, responseListener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_GET_REQUESTS_TIMEOUT, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
