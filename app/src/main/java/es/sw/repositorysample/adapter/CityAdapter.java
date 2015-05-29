package es.sw.repositorysample.adapter;

import android.content.Context;
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

    private WeakReference<Context>contextWeakReference;
    private List<City> cityList = new ArrayList<>();

    public CityAdapter(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        notifyDataSetChanged();
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
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(contextWeakReference.get()).inflate(R.layout.row_city, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        City city = getItem(i);
        holder.nameTV.setText(city.getName());
        holder.idTV.setText(String.valueOf(city.getId()));

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.city_name) TextView nameTV;
        @InjectView(R.id.city_id) TextView idTV;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
