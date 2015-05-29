package es.sw.repositorysample.net.volley.jsonobject;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import es.sw.repositorysample.net.volley.ResponseListener;
import es.sw.repositorysample.net.volley.ServerResponse;
import es.sw.repositorysample.net.volley.VolleySingleton;

import static es.sw.repositorysample.BuildConfig.DEBUG;

/**
 * Created by ivancarreira on 26/03/15.
 */
public abstract class JsonObjectRequest<T,J> extends Request<ServerResponse<T, J>>{
    public static final String TAG = JsonObjectRequest.class.getSimpleName();

    private static final String DEFAULT_CHARSET = "UTF-8";
    private final Map<String, String> headers;
    private final ResponseListener<T, J> responseListener;

    /**
     * Make a request and return a parsed object from JSON.
     * @param method
     * @param url
     * @param headers
     * @param responseListener
     * @param errorListener
     */
    public JsonObjectRequest(int method, String url, Map<String, String> headers, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.headers = headers;
        this.responseListener = responseListener;
    }

    /**
     *  Make a request and return a parsed object from JSON.
     * @param method
     * @param url
     * @param responseListener
     * @param errorListener
     */
    public JsonObjectRequest(int method, String url, ResponseListener<T, J> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        headers = null;
        this.responseListener = responseListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    /**
     * runs on uithread
     * @param response
     */
    @Override
    protected void deliverResponse(ServerResponse<T, J> response) {
        if (response.wasCompletedSuccessfully()) {
            deliverSuccess(response.getSuccessData());
        } else {
            deliverFailure(response.getNotSuccessData());
        }
    }

    /**
     * invoke when server responds success
     * @param successResponse
     */
    protected void deliverSuccess(T successResponse){
        if (responseListener != null){
            responseListener.onSuccess(successResponse);
        } else {
            Log.i(TAG, "responseListener is null");
        }
    }

    /**
     * invokes when server responds false
     * @param failureResponse
     */
    protected void deliverFailure(J failureResponse){
        if (responseListener != null){
            responseListener.onError(failureResponse);
        } else {
            Log.i(TAG, "responseListener is null");
        }
    }

    /**
     * runs on background thread
     * @param response
     * @return
     */
    @Override
    protected Response<ServerResponse<T, J>> parseNetworkResponse(NetworkResponse response) {
        try {
            String charset = HttpHeaderParser.parseCharset(response.headers);
            if (charset == null){
                charset = DEFAULT_CHARSET;
            }
            String json = new String(response.data, charset);
            JSONObject jsonObject = new JSONObject(json);
            if (DEBUG){
                Log.e(TAG, jsonObject.toString(8));
            }
            ServerResponse<T, J> object = parse(jsonObject);
            return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e){
            if (DEBUG) {
                e.printStackTrace();
            }
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            if (DEBUG) {
                e.printStackTrace();
            }
            return Response.error(new ParseError(e));
        }
    }

    /**
     * Starts petition
     */
    public void start(){
        tryStart();
    }


    private void tryStart(){
        try {
            startWebservice();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void startWebservice() {
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        requestQueue.add(this);
    }


    /**
     * Cancel all petitions with this tag
     * @param webserviceTag
     */
    public static void cancel(final String webserviceTag){
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        if (webserviceTag == null || requestQueue == null){
            Log.i(TAG, "cancel didnt found webservice with tag: " + webserviceTag);
            return;
        }

        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (request.getTag() != null && request.getTag().equals(webserviceTag)){
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * abstrat method to parse content into a server response object impl
     * @param jsonObject
     * @return
     */
    protected abstract ServerResponse<T, J> parse(JSONObject jsonObject);


}
