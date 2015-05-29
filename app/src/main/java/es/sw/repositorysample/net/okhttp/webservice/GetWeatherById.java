package es.sw.repositorysample.net.okhttp.webservice;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import es.sw.repositorysample.domain.server.WeatherCloud;
import es.sw.repositorysample.net.okhttp.OkHttp;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class GetWeatherById implements WebserviceResponse<WeatherCloud, Long>{

    private OkHttp okHttp;

    @Inject
    public GetWeatherById(OkHttp okHttp) {
        this.okHttp = okHttp;
    }

    @Override
    public WeatherCloud start(Long params) throws IOException, JSONException {
        String url = "http://api.openweathermap.org/data/2.5/weather?id=";
        Request request = new Request.Builder()
                .url(url+ params)
                .tag(url)
                .build();

        Response response = okHttp.execute(request);
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        ResponseBody body = response.body();
        JSONObject jsonObject = new JSONObject(body.string());
        WeatherCloud weatherCloud = new WeatherCloud(jsonObject);
        return weatherCloud;
    }
}
