package es.sw.repositorysample.domain.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import es.sw.repositorysample.database.ActiveRecord;

/**
 * Created by albertopenasamor on 27/5/15.
 */
@DatabaseTable
public class WeatherTable extends ActiveRecord {

    public static final String REMOTE_ID = "remote_id";

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(unique = true, columnName = REMOTE_ID)
    private long remoteId;

    @DatabaseField
    private double latitude;

    @DatabaseField
    private double longitude;

    @DatabaseField
    private double temp;

    @DatabaseField
    private double pressure;

    @DatabaseField
    private double humidity;

    @DatabaseField
    private double windSpeed;

    public WeatherTable(){}

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
