package es.sw.repositorysample.domain.dummy;

import java.util.ArrayList;
import java.util.List;

import es.sw.repositorysample.domain.model.City;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class DummyCities {

    public static List<City> cityList = new ArrayList<>();

    static {
        City city = new City(City.CURRENT_POSITION, "Tú localización");
        cityList.add(city);

        city = new City(3675707, "Madrid");
        cityList.add(city);

        city = new City(3648559, "Barcelona");
        cityList.add(city);

        city = new City(3625549, "Valencia");
        cityList.add(city);

        city = new City(3119841, "A Coruña");
        cityList.add(city);

        city = new City(3668132, "Sevilla");
        cityList.add(city);

        city = new City(3105976, "Vigo");
        cityList.add(city);

        city = new City(3665566, "Zaragoza");
        cityList.add(city);

        city = new City(2521886, "Almería");
        cityList.add(city);

        city = new City(1722433, "Burgos");
        cityList.add(city);

        city = new City(2520600, "Cadiz");
        cityList.add(city);

        city = new City(1713004, "Gerona");
        cityList.add(city);
    }

    public static City findById(long id) throws Exception{
        for (City city:cityList){
            if (city.getId() == id){
                return city;
            }
        }
        throw new NullPointerException("Couldnt find city");
    }
}
