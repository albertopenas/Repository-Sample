package es.sw.repositorysample.net.volley.multipart;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivancarreira on 11/05/15.
 */
public abstract class BaseMultipartRequest extends Request {
    public static final String TAG = BaseMultipartRequest.class.getSimpleName();

    private HttpEntity mHttpEntity;
    private Map<String, String> mHeaders;
    private Response.Listener mListener;

    public BaseMultipartRequest(int method, String url,
                                Map<String, File> filesToUpload,
                                Map<String, String> stringParams,
                                Response.Listener listener,
                                Response.ErrorListener errorListener) throws AuthFailureError{
        super(method, url, errorListener);
        mHttpEntity = buildMultipartEntity(filesToUpload, stringParams);
        mListener = listener;
        //mHeaders = super.getHeaders();
    }

    private HttpEntity buildMultipartEntity(Map<String, File> filesToUpload, Map<String, String> stringParams) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (filesToUpload != null) {
            for (Map.Entry<String, File> entry : filesToUpload.entrySet()) {
                String fileName = entry.getValue().getName();
                Log.d(TAG, "build MultipartEntity, filename: " + fileName);
                builder.addPart(entry.getKey(), new FileBody(entry.getValue(), ContentType.create("image/jpeg"), fileName));
            }
        }
        if (stringParams != null) {
            for (Map.Entry<String, String> entry : stringParams.entrySet()) {
                ContentType contentType = ContentType.create("text/plain", MIME.UTF8_CHARSET);
                builder.addPart(entry.getKey(), new StringBody(entry.getValue(), contentType));
            }
        }

        return builder.build();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Log.d(TAG, "getHeaders()");
        if (mHeaders == null) {
            Log.d(TAG, "mHeaders is null");
            mHeaders = new HashMap<String, String>();
        }

        mHeaders.put("Content-type", mHttpEntity.getContentType().getValue());

        for (Map.Entry entry : mHeaders.entrySet()) {
            Log.d(TAG, "getHeaders key: " + (String)entry.getKey() + " value: " + (String)entry.getValue());
        }

        return super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        Log.d(TAG, "getBodyContentType: " + mHttpEntity.getContentType().getValue());

        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {
        Log.d(TAG, "getBody!!!!!!!! ContentLength: " + mHttpEntity.getContentLength());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(byteArrayOutputStream);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException jsonException) {
            return Response.error(new ParseError(jsonException));
        }
    }

    @Override
    protected void deliverResponse(Object response) {
        if(mListener != null) {
            mListener.onResponse(response);
        }
    }
}
