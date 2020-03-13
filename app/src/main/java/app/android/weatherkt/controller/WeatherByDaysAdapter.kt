package app.android.weatherkt.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.android.weatherkt.R
import app.android.weatherkt.model.X

class WeatherByDaysAdapter : RecyclerView.Adapter<WeatherByDaysAdapter.ViewHolder>() {

    private var weatherList: List<X> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_by_days, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return weatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    fun submitList(list: List<X>) {

        weatherList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}