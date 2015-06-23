package es.sw.repositorysample.domain.model;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class City {

    public static final long CURRENT_POSITION = -1;

    private long id;
    private String name;

    public City(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public boolean isCurrentPosition(){
        return id == CURRENT_POSITION;
    }
}
