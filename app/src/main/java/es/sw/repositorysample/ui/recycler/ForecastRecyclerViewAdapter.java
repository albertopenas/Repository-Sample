package es.sw.repositorysample.ui.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.sw.repositorysample.R;
import es.sw.repositorysample.domain.model.WeatherForecast;
import es.sw.repositorysample.helper.DayFormatter;
import es.sw.repositorysample.helper.TemperatureFormatter;

/**
 * Created by albertopenasamor on 23/6/15.
 */
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> implements View.OnClickListener {

    private List<WeatherForecast> weatherForecastList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private DayFormatter dayFormatter;

    public ForecastRecyclerViewAdapter(DayFormatter dayFormatter) {
        this.dayFormatter = dayFormatter;
    }

    public void setWeatherForecastList(List<WeatherForecast> weatherForecastList) {
        this.weatherForecastList = weatherForecastList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_forecast, parent, false);
        v.setOnClickListener(this);
        return new ForecastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        final WeatherForecast weatherForecast = weatherForecastList.get(position);
        final String day = dayFormatter.format(weatherForecast.getTimestamp());
        holder.dayTV.setText(day);
        holder.descriptionTV.setText(weatherForecast.getDescription());
        holder.maximumTemperatureTV.setText(TemperatureFormatter.format(weatherForecast.getMaximumTemperature()));
        holder.minimumTemperatureTV.setText(TemperatureFormatter.format(weatherForecast.getMinimumTemperature()));
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null){
            onItemClickListener.onItemClick(view);
        }
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView dayTV;
        private TextView descriptionTV;
        private TextView maximumTemperatureTV;
        private TextView minimumTemperatureTV;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            dayTV = (TextView) itemView.findViewById(R.id.day);
            descriptionTV = (TextView) itemView.findViewById(R.id.description);
            maximumTemperatureTV = (TextView) itemView.findViewById(R.id.maximum_temperature);
            minimumTemperatureTV = (TextView) itemView.findViewById(R.id.minimum_temperature);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view);
    }
}
