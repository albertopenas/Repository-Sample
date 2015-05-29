package es.sw.repositorysample.net.okhttp.webservice;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public interface WebserviceResponse<T, J> {
    T start(J params) throws IOException, JSONException;
}
