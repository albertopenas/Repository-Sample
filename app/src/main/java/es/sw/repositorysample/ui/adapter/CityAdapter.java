package es.sw.repositorysample.ui.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.sw.repositorysample.R;
import es.sw.repositorysample.domain.model.City;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class CityAdapter extends BaseAdapter {

    private static final int ROW_CURRENT_LOCATION = 0;
    private static final int ROW_CITY = ROW_CURRENT_LOCATION + 1;
    private WeakReference<Context>contextWeakReference;
    private List<City> cityList = new ArrayList<>();
    private Location location;
    public CityAdapter(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City getItem(int i) {
        return cityList.get(i);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        City city = getItem(position);
        return city.isCurrentPosition()?ROW_CURRENT_LOCATION:ROW_CITY;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);

        if (view == null){
            if (itemViewType == ROW_CURRENT_LOCATION){
                view = LayoutInflater.from(contextWeakReference.get()).inflate(R.layout.row_current_position, viewGroup, false);
                CurrentLocationViewHolder holder = new CurrentLocationViewHolder(view);
                view.setTag(holder);
            }else{
                view = LayoutInflater.from(contextWeakReference.get()).inflate(R.layout.row_city, viewGroup, false);
                CityViewHolder holder = new CityViewHolder(view);
                view.setTag(holder);
            }
        }

        if (itemViewType == ROW_CURRENT_LOCATION){
            if(location != null){
                CurrentLocationViewHolder holder = (CurrentLocationViewHolder) view.getTag();
                holder.positionTextView.setText(String.format("%s, %s", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
            }
        }else {
            City city = getItem(i);
            CityViewHolder holder = (CityViewHolder) view.getTag();
            holder.nameTV.setText(city.getName());
            holder.idTV.setText(String.valueOf(city.getId()));
        }

        return view;
    }

    static class CityViewHolder {
        @InjectView(R.id.city_name) TextView nameTV;
        @InjectView(R.id.city_id) TextView idTV;

        public CityViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class CurrentLocationViewHolder {
        @InjectView(R.id.current_position_tv) TextView positionTextView;

        public CurrentLocationViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
