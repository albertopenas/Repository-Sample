package es.sw.repositorysample.net.volley.jsonobject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

import es.sw.repositorysample.net.volley.ResponseListener;


/**
 * Created by ivancarreira on 27/03/15.
 */
public abstract class PostJsonObjectRequest<T, J> extends JsonObjectRequest<T, J> {
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
    public PostJsonObjectRequest(String url, Map<String, String> headers, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, headers, responseListener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_POST_REQUESTS_TIMEOUT, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public PostJsonObjectRequest(String url, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, null, responseListener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_POST_REQUESTS_TIMEOUT, MAX_NUM_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Log.d(TAG, "getBody()");
        JSONObject jsonObject = getJsonObjectBody();

        if (jsonObject != null) {
            return jsonObject.toString().getBytes();
        }
        return super.getBody();
    }

    public abstract JSONObject getJsonObjectBody();
}
