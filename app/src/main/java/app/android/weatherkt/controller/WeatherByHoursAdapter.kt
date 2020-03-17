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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class WeatherByHoursAdapter : RecyclerView.Adapter<WeatherByHoursAdapter.ViewHolder>() {

    private var weatherList: List<X> = ArrayList()
    private val nightHours: ArrayList<String> = ArrayList()
    private val degree = "\u00B0"

    init {
        nightHours.add("20")
        nightHours.add("21")
        nightHours.add("22")
        nightHours.add("23")
        nightHours.add("00")
        nightHours.add("01")
        nightHours.add("02")
        nightHours.add("03")
        nightHours.add("04")
        nightHours.add("05")
        nightHours.add("06")
    }

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
            tv1.text = parseTimeStamp(x.dt, 2)

            val tv2 = tvTemp
            txt = x.main.temp_max.roundToInt().toString() + degree
            tv2.text = txt

            val iv = ivIcon
            val isNight: Boolean = parseTimeStamp(x.dt, 1) in nightHours
            weatherIcon(x.weather[0].main, iv, isNight)
        }
    }

    fun parseTimeStamp(dt: Int, type: Int): String {
        var simpleDataFormat = SimpleDateFormat()
        when (type) {
            1 -> simpleDataFormat = SimpleDateFormat("HH")
            2 -> simpleDataFormat = SimpleDateFormat("HH:mm")
        }

        return simpleDataFormat.format(Date(dt.toLong() * 1000))
    }

    fun weatherIcon(weatherType: String, imageView: ImageView, isNight: Boolean) {

        if (isNight) {
            when (weatherType) {
                "Clear" -> imageView.setImageResource(R.drawable.ic_white_night_bright)
                "Clouds" -> imageView.setImageResource(R.drawable.ic_white_night_cloudy)
                "Thunderstorm" -> imageView.setImageResource(R.drawable.ic_white_night_thunder)
                "Drizzle" -> imageView.setImageResource(R.drawable.ic_white_night_rain)
                "Rain" -> imageView.setImageResource(R.drawable.ic_white_night_shower)
            }
        } else {
            when (weatherType) {
                "Clear" -> imageView.setImageResource(R.drawable.ic_white_day_bright)
                "Clouds" -> imageView.setImageResource(R.drawable.ic_white_day_cloudy)
                "Thunderstorm" -> imageView.setImageResource(R.drawable.ic_white_day_thunder)
                "Drizzle" -> imageView.setImageResource(R.drawable.ic_white_day_rain)
                "Rain" -> imageView.setImageResource(R.drawable.ic_white_day_shower)
            }
        }
    }
}