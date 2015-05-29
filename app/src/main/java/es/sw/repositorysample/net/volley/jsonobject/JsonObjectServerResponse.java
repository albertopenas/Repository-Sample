package es.sw.repositorysample.net.volley.jsonobject;

import org.json.JSONObject;

import es.sw.repositorysample.net.volley.ServerResponse;


/**
 * Created by ivancarreira on 27/03/15.
 */
public abstract class JsonObjectServerResponse<T, J> implements ServerResponse<T, J> {

    protected boolean success;

    public void setWasCompleted(boolean success) {
        this.success = success;
    }

    @Override
    public boolean wasCompletedSuccessfully() {
        return success;
    }

    public JsonObjectServerResponse(JSONObject jsonObject){
        parse(jsonObject);
    }

    public abstract void parse(JSONObject jsonObject);
}
