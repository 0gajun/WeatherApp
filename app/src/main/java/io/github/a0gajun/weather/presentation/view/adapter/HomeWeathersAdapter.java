/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.adapter;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.databinding.LayoutWeatherAndForecastCardBinding;
import io.github.a0gajun.weather.databinding.LayoutWeatherAndForecastHeaderBinding;
import io.github.a0gajun.weather.domain.model.CurrentWeather;
import io.github.a0gajun.weather.domain.model.CurrentWeatherAndForecast;
import io.github.a0gajun.weather.domain.model.FiveDayForecast;
import lombok.Getter;
import timber.log.Timber;

/**
 * Created by Junya Ogasawara on 1/14/17.
 */

public class HomeWeathersAdapter extends RecyclerView.Adapter<HomeWeathersAdapter.BindingHolder> {

    private static final int FORECAST_MAX_ENTRY = 8;
    private final LayoutInflater layoutInflater;
    private List<AbstractDataHolder> dataHolders;

    @Inject
    public HomeWeathersAdapter(Activity context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        this.dataHolders = Collections.EMPTY_LIST;
    }

    public void setCurrentWeatherAndForecasts(Collection<CurrentWeatherAndForecast> currentWeatherAndForecasts) {
        List<AbstractDataHolder> list = new ArrayList<>();
        list.add(HeaderHolder.REGISTERED_LOCATIONS);
        this.dataHolders = Stream.of(currentWeatherAndForecasts)
                .reduce(list, (acc, weather) -> {
                    if (weather.isCurrentLocation()) {
                        acc.add(0, HeaderHolder.CURRENT_LOCATION);
                        acc.add(1, new DataHolder(weather));
                    } else {
                        acc.add(new DataHolder(weather));
                    }
                    return acc;
                });

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return this.dataHolders.get(position).viewType.value;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewType type = ViewType.getViewType(viewType)
                .orElseThrow(() -> new RuntimeException("Invalid view Type."));

        View view;
        switch (type) {
            case HEADER:
                view = this.layoutInflater.inflate(R.layout.layout_weather_and_forecast_header, parent, false);
                break;
            case CURRENT_LOCATION:
            case REGISTERED_LOCATION:
                view = this.layoutInflater.inflate(R.layout.layout_weather_and_forecast_card, parent, false);
                break;
            default:
                throw new IllegalStateException("Normally, never reach here...");
        }

        return new BindingHolder(view);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        AbstractDataHolder dataHolder = dataHolders.get(position);
        switch (dataHolder.viewType) {
            case HEADER:
                setUpHeaderView(holder, (HeaderHolder) dataHolder);
                break;
            case CURRENT_LOCATION:
            case REGISTERED_LOCATION:
                setUpCardView(holder, (DataHolder) dataHolder);
                break;
        }
    }

    private void setUpHeaderView(BindingHolder bindingHolder, HeaderHolder headerHolder) {
        final LayoutWeatherAndForecastHeaderBinding binding
                = (LayoutWeatherAndForecastHeaderBinding) bindingHolder.getBinding();
        final Context context = bindingHolder.itemView.getContext();

        binding.headerTxt.setText(headerHolder.getTxt());
        bindingHolder.itemView.setBackgroundColor(context.getResources().getColor(headerHolder.getColorResId()));
    }

    private void setUpCardView(BindingHolder bindingHolder, DataHolder dataHolder) {
        final CurrentWeatherAndForecast data = dataHolder.currentWeatherAndForecast;
        final CurrentWeather weather = data.getCurrentWeather();
        final FiveDayForecast forecast = data.getFiveDayForecast();
        final LayoutWeatherAndForecastCardBinding binding = (LayoutWeatherAndForecastCardBinding) bindingHolder.getBinding();
        final Context context = bindingHolder.itemView.getContext();
        binding.locationTxt.setText(data.getCityName() + "," + data.getCountryName());
        binding.temperature.setText(context.getString(R.string.celsius_degree_fmt, weather.getTemperature()));
        binding.weather.setText(weather.getWeather());
        binding.weatherDesc.setText(weather.getWeatherDescription());
        binding.weatherIcon.setImageResource(weather.getWeatherIconResId());

        setUpRecyclerView(binding, context, forecast);

        bindingHolder.itemView.setOnClickListener(v -> {
            Timber.d("Onclicked!");
            EventBus.getDefault().post(new ViewDetailWeatherEvent(data.getZipCode()));
        });
    }

    private void setUpRecyclerView(LayoutWeatherAndForecastCardBinding binding, Context context, FiveDayForecast forecast) {
        binding.rvForecast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        EveryThreeHoursForecastAdapter adapter = new EveryThreeHoursForecastAdapter(context);
        binding.rvForecast.setAdapter(adapter);
        adapter.setForecastDataList(extractForecastDataList(forecast));
        adapter.notifyDataSetChanged();
    }

    private List<FiveDayForecast.EveryThreeHoursForecastData> extractForecastDataList(FiveDayForecast forecast) {
        if (forecast.getForecastData().size() < FORECAST_MAX_ENTRY) {
            return new ArrayList<>(forecast.getForecastData());
        }
        return new ArrayList<>(forecast.getForecastData().subList(0, FORECAST_MAX_ENTRY));
    }

    /**
     * @param position
     * @return Deleted data
     */
    public synchronized CurrentWeatherAndForecast deleteItem(int position) {
        AbstractDataHolder dataHolder = this.dataHolders.get(position);
        if (dataHolder == null || dataHolder.viewType != ViewType.REGISTERED_LOCATION) {
            return null;
        }
        final CurrentWeatherAndForecast data = ((DataHolder) dataHolder).getCurrentWeatherAndForecast();

        this.dataHolders.remove(position);
        this.notifyItemRemoved(position);

        return data;
    }

    @Override
    public int getItemCount() {
        return this.dataHolders.size();
    }

    public enum ViewType {
        HEADER(1),
        CURRENT_LOCATION(2),
        REGISTERED_LOCATION(3);

        public final int value;

        ViewType(int v) {
            this.value = v;
        }

        public static Optional<ViewType> getViewType(int value) {
            return Stream.of(ViewType.values()).filter(type -> type.value == value).findFirst();
        }
    }

    static abstract class AbstractDataHolder {
        final ViewType viewType;

        AbstractDataHolder(ViewType viewType) {
            this.viewType = viewType;
        }
    }

    private static class HeaderHolder extends AbstractDataHolder {
        static final HeaderHolder REGISTERED_LOCATIONS = new HeaderHolder("Your watching Locations", R.color.holo_blue_dark);
        static final HeaderHolder CURRENT_LOCATION = new HeaderHolder("Current Location", R.color.holo_orange_dark);

        @Getter private final String txt;
        @Getter @ColorRes private final int colorResId;

        private HeaderHolder(String txt, @ColorRes int colorResId) {
            super(ViewType.HEADER);
            this.txt = txt;
            this.colorResId = colorResId;
        }
    }

    private static class DataHolder extends AbstractDataHolder {
        @Getter private final CurrentWeatherAndForecast currentWeatherAndForecast;

        DataHolder(CurrentWeatherAndForecast currentWeatherAndForecast) {
            super(currentWeatherAndForecast.isCurrentLocation() ? ViewType.CURRENT_LOCATION : ViewType.REGISTERED_LOCATION);
            this.currentWeatherAndForecast = currentWeatherAndForecast;
        }
    }

    static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        BindingHolder(View itemView) {
            super(itemView);
            this.binding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

    public static class HomeWeathersItemTouchHelperCallback extends ItemTouchHelper.Callback {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == ViewType.REGISTERED_LOCATION.value) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT);
            }
            return 0;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            EventBus.getDefault().post(new RecyclerViewItemDeleteEvent(viewHolder.getAdapterPosition()));
        }
    }

    public static class RecyclerViewItemDeleteEvent {
        public final int deletedItemsAdapterPosition;

        RecyclerViewItemDeleteEvent(int deletedItemsAdapterPosition) {
            this.deletedItemsAdapterPosition = deletedItemsAdapterPosition;
        }
    }

    public static class ViewDetailWeatherEvent {
        public final String zipCode;

        ViewDetailWeatherEvent(String zipCode) {
            this.zipCode = zipCode;
        }
    }
}
