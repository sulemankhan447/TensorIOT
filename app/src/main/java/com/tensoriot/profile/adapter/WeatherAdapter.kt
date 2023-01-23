package com.tensoriot.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tensoriot.databinding.ItemWeatherBinding
import com.tensoriot.model.WeatherMainModel
import com.tensoriot.utils.DateUtils

class WeatherAdapter() :
    ListAdapter<WeatherMainModel, WeatherAdapter.WeatherViewHolder>(ComparatorDiffUtil()) {


    inner class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: WeatherMainModel) {
            binding.tvDay.text = DateUtils.convertTimeStampToDay(note.dt ?: 0L)
            binding.tvTemp.text = "Temp : ${note.temp}"
            binding.tvMaxTemp.text = "Max Temp : ${note.temp_max}"
            binding.tvMinTemp.text = "Min Temp : ${note.temp_min}"

        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<WeatherMainModel>() {
        override fun areItemsTheSame(
            oldItem: WeatherMainModel,
            newItem: WeatherMainModel
        ): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: WeatherMainModel,
            newItem: WeatherMainModel
        ): Boolean {
            return oldItem.temp.equals(newItem.temp, true)
                    && oldItem.temp_min.equals(newItem.temp_min, true)
                    && oldItem.temp_max.equals(newItem.temp_max, true)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }


}