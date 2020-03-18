package app.android.weatherkt.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.android.weatherkt.R
import app.android.weatherkt.model.WeatherContainer
import kotlinx.android.synthetic.main.item_by_days.view.*
import kotlin.math.roundToInt

class WeatherByDaysAdapter : RecyclerView.Adapter<WeatherByDaysAdapter.ViewHolder>() {

    private var weatherList: List<WeatherContainer> = ArrayList()
    private val weatherController: WeatherController = WeatherController()
    private val degree = "\u00B0"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_by_days, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    fun submitList(list: List<WeatherContainer>) {
        weatherList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var tvDay: TextView = view.tv_day_item_by_days
        private var tvTemp: TextView = view.tv_temp_item_by_days
        private var ivIcon: ImageView = view.iv_item_by_days
        private lateinit var temp: String

        fun bind(weatherContainer: WeatherContainer) {
            val tv1 = tvDay
            tv1.text = weatherContainer.dayOfWeek

            val tv2 = tvTemp
            temp = "${weatherContainer.tempMax.roundToInt()}$degree / ${weatherContainer.tempMin.roundToInt()}$degree"
            tv2.text = temp

            val iv = ivIcon
            weatherController.weatherIcon(weatherContainer.weatherType, iv, -1)
            Log.e("WeatherKt","weatherType: ${weatherContainer.weatherType}")
        }
    }
}