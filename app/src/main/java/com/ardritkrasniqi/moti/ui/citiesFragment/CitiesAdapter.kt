package com.ardritkrasniqi.moti.ui.citiesFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.databinding.CityListItemBinding
import com.ardritkrasniqi.moti.domain.WeatherForecastModel


class CitiesAdapter : ListAdapter<WeatherForecastModel, CitiesAdapter.CityViewHolder>(DiffCallback) {


    object DiffCallback : DiffUtil.ItemCallback<WeatherForecastModel>() {
        override fun areItemsTheSame(oldItem:WeatherForecastModel, newItem: WeatherForecastModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherForecastModel, newItem: WeatherForecastModel): Boolean {
            return oldItem == newItem
        }

    }


    class CityViewHolder(var binding: CityListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cityName: WeatherForecastModel) {
            binding.cityNameList = cityName.city.name
            binding.temperature = cityName.weatherList?.get(0)?.temp.toString()
            binding.description = cityName.weatherList?.get(0)?.main
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CityViewHolder(CityListItemBinding.inflate(layoutInflater,parent,false))
    }


    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
    }
}
