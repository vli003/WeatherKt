package app.android.weatherkt.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.android.weatherkt.R
import app.android.weatherkt.model.X
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_by_hours.view.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class WeatherByHoursAdapter : RecyclerView.Adapter<WeatherByHoursAdapter.ViewHolder>() {

    private var weatherList: List<X> = ArrayList()

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

        var tvTime: TextView = view.tv_time_item_by_hours
        var tvTemp: TextView = view.tv_temp_item_by_hours
        var ivIcon: ImageView = view.iv_item_by_hours

        fun bind(x: X) {
            val tv1 = tvTime
            tv1.text = parseTimeStamp(x.dt)

            val tv2 = tvTemp
            tv2.text = x.main.temp_max.roundToInt().toString() +"\u2103"

            val iv = ivIcon
            weatherIcon(x.weather[0].main, iv)
        }
    }

    fun parseTimeStamp(dt: Int): String {
        val simpleDataFormat = SimpleDateFormat("HH:mm")
        return simpleDataFormat.format(Date(dt.toLong() * 1000))
    }

    fun weatherIcon(weatherType: String, imageView: ImageView) {
        when (weatherType) {
            "Clear" -> imageView.setImageResource(R.drawable.ic_white_day_bright)
            "Clouds" -> imageView.setImageResource(R.drawable.ic_white_day_cloudy)
        }
    }
}