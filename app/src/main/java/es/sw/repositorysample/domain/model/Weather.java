package es.sw.repositorysample.domain.model;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class Weather {

    private long id;
    private long remoteId;
    private double latitude;
    private double longitude;
    private double temp;
    private double pressure;
    private double humidity;
    private double windSpeed;

    public Weather() {}

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}
