package es.sw.repositorysample.domain.server;

import org.json.JSONObject;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class WeatherCloud {

    private long remoteId;
    private double latitude;
    private double longitude;
    private double temp;
    private double pressure;
    private double humidity;
    private double windSpeed;

    public WeatherCloud(JSONObject jsonObject) {
        this.remoteId = jsonObject.optInt("id");
        JSONObject coordJsonObject = jsonObject.optJSONObject("coord");
        if (coordJsonObject != null){
            this.latitude = coordJsonObject.optDouble("lat");
            this.longitude = coordJsonObject.optDouble("lon");
        }
        JSONObject mainJsonObject = jsonObject.optJSONObject("main");
        if (mainJsonObject != null){
            this.temp = mainJsonObject.optDouble("temp");
            this.pressure = mainJsonObject.optDouble("pressure");
            this.humidity = mainJsonObject.optDouble("humidity");
        }
        JSONObject windJsonObject = jsonObject.optJSONObject("wind");
        if (windJsonObject != null){
            this.windSpeed = windJsonObject.optDouble("speed");
        }
    }

    public long getRemoteId() {
        return remoteId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
