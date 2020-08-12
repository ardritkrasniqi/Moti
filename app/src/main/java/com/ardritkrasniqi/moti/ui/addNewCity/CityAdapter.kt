package com.ardritkrasniqi.moti.ui.addNewCity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.network.CityData

class CityAdapter(
    private val cityList: MutableList<CityData>,
    private val listener: (CityData) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityName = itemView.findViewById<TextView>(R.id.city_name_prefix)
        var countryName = itemView.findViewById<TextView>(R.id.country_name_prefix)
        val cityRegiion = itemView.findViewById<TextView>(R.id.city_region_prefix)

        fun bind(item: CityData) {
            cityName.text = item.city
            countryName.text = item.countryCode
            cityRegiion.text = item.region
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.prefix_cities_list,
            parent,
            false
        )
        return CityViewHolder(itemView)
    }

    override fun getItemCount() = cityList.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val currentItem = cityList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { listener(currentItem) }
    }

}

