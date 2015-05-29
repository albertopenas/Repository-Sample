package es.sw.repositorysample.net.volley.jsonobject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import java.util.Map;

import es.sw.repositorysample.net.volley.ResponseListener;


/**
 * Created by ivancarreira on 06/05/15.
 */
public abstract class DeleteJsonObjectRequest<T, J>  extends JsonObjectRequest<T, J> {
    private static final String TAG = PostJsonObjectRequest.class.getSimpleName();
    private static final int DEFAULT_POST_REQUESTS_TIMEOUT = 25000;
    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";
    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private static final int MAX_NUM_RETRIES = 0;

    /**
     * Make a request and return a parsed object from JSON.
     *
     * @param url
     * @param headers
     * @param responseListener
     * @param errorListener
     */
    public DeleteJsonObjectRequest(String url, Map<String, String> headers, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(Method.DELETE, url, headers, responseListener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_POST_REQUESTS_TIMEOUT, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public DeleteJsonObjectRequest(String url, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(Method.DELETE, url, null, responseListener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_POST_REQUESTS_TIMEOUT, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
