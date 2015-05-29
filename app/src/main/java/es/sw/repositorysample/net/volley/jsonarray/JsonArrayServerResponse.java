package es.sw.repositorysample.net.volley.jsonarray;

import org.json.JSONArray;

import es.sw.repositorysample.net.volley.ServerResponse;


/**
 * Created by ivancarreira on 27/03/15.
 */
public abstract class JsonArrayServerResponse<T, J> implements ServerResponse<T, J> {

    protected boolean success;

    public void setWasCompleted(boolean success) {
        this.success = success;
    }

    @Override
    public boolean wasCompletedSuccessfully() {
        return success;
    }

    protected JsonArrayServerResponse(JSONArray jsonArray){
        parse(jsonArray);
    }

    public abstract void parse(JSONArray jsonArray);
}
