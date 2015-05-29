package es.sw.repositorysample.domain.model;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class City {
    private long id;
    private String name;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
