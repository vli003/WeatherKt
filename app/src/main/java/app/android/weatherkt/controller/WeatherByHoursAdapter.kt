package app.android.weatherkt.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.android.weatherkt.R
import app.android.weatherkt.model.X
import kotlinx.android.synthetic.main.item_by_hours.view.*
import kotlin.math.roundToInt

class WeatherByHoursAdapter : RecyclerView.Adapter<WeatherByHoursAdapter.ViewHolder>() {

    private var weatherList: List<X> = ArrayList()
    private val degree = "\u00B0"
    private val weatherController: WeatherController = WeatherController()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_by_hours, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    fun submitList(list: List<X>) {
        weatherList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var tvTime: TextView = view.tv_time_item_by_hours
        private var tvTemp: TextView = view.tv_temp_item_by_hours
        private var ivIcon: ImageView = view.iv_item_by_hours
        private lateinit var txt: String

        fun bind(x: X) {
            val tv1 = tvTime
            tv1.text = weatherController.parseTimeStamp(x.dt, "HH:mm")

            val tv2 = tvTemp
            txt = x.main.temp_max.roundToInt().toString() + degree
            tv2.text = txt

            val iv = ivIcon
            weatherController.weatherIcon(x.weather[0].main, iv, x.dt)
        }
    }
}