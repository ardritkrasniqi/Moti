package com.ardritkrasniqi.moti.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.databinding.CityListItemBinding
import com.ardritkrasniqi.moti.domain.WeatherForecastModel


class CitiesAdapter(
    val clickListener: OnClickListener
) : ListAdapter<WeatherForecastModel, CitiesAdapter.CityViewHolder>(
    DiffCallback
) {


    object DiffCallback : DiffUtil.ItemCallback<WeatherForecastModel>() {
        override fun areItemsTheSame(oldItem:WeatherForecastModel, newItem: WeatherForecastModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherForecastModel, newItem: WeatherForecastModel): Boolean {
            return oldItem == newItem
        }

    }


    inner class CityViewHolder(var binding: CityListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(
            cityName: WeatherForecastModel,
            clickListener: OnClickListener
        ) {
            binding.cityNamee = cityName.city.name
            binding.temperature = cityName.weatherList?.get(0)?.temp.toString()
            binding.description = cityName.weatherList?.get(0)?.main
            binding.clickListener = clickListener
            binding.imageResource = (
                when(cityName.weatherList?.get(0)?.weatherId){
                    in 200..599 -> R.drawable.rainy_card
                    in 600..699 -> R.drawable.snowy_card
                    in 700..799 -> R.drawable.cloudy_card
                    800 -> R.drawable.sunny_card
                    in 801..900 -> R.drawable.cloudy_card
                    else -> R.drawable.snowy_card
                }
            )
            binding.backgroundColor = (
                    when(cityName.weatherList?.get(0)?.weatherId){
                        in 200..599 -> R.color.rainyCardBackground
                        in 600..699 -> R.color.snowyCardBackground
                        in 700..799 -> R.color.cloudyCardBackGround
                        800 -> R.color.sunnyCardBackground
                        in 801..900 -> R.color.cloudyCardBackGround
                        else -> R.color.sunnyCardBackground
                    }
                    )
            binding.executePendingBindings()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CityViewHolder(
            CityListItemBinding.inflate(layoutInflater, parent, false)
        )
    }


    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class OnClickListener(val clickListener: (cityName: String) -> Unit, val clickLongListener: (cityName: String) -> Unit){
    fun onClick(cityName: String) = clickListener(cityName)
    fun onLongClick(cityName: String) = clickLongListener(cityName)
}

