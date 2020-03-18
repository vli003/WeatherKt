package app.android.weatherkt.controller

import android.util.Log
import android.widget.ImageView
import app.android.weatherkt.R
import app.android.weatherkt.model.X
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherController {

    private val nightHours: ArrayList<String> = ArrayList()

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

/*    fun parseTimeStamp(dt: Int): String {
        val simpleDataFormat = SimpleDateFormat("EE, dd MMMM")
        return simpleDataFormat.format(Date(dt.toLong() * 1000))
    }*/

    fun parseTimeStamp(dt: Int, type: Int): String {
        var simpleDataFormat = SimpleDateFormat()
        when (type) {
            1 -> simpleDataFormat = SimpleDateFormat("HH")
            2 -> simpleDataFormat = SimpleDateFormat("HH:mm")
            3 -> simpleDataFormat = SimpleDateFormat("aa")
            4 -> simpleDataFormat = SimpleDateFormat("EE, dd MMMM")
            5 -> simpleDataFormat = SimpleDateFormat("EE")
        }
        return simpleDataFormat.format(Date(dt.toLong() * 1000))
    }

    fun getDateFromTimeStamp(dt: Int): Int {
        val simpleDataFormat = SimpleDateFormat("dd")
        return simpleDataFormat.format(Date(dt.toLong() * 1000)).toInt()
    }

    fun weatherIcon(weatherType: String, iv: ImageView, dt: Int) {
        val isNight: Boolean = if (dt == -1) false
        else parseTimeStamp(dt, 1) in nightHours

//        val isNight2: Boolean = parseTimeStamp(dt, 1) in nightHours

        if (isNight) {
            when (weatherType) {
                "Clear" -> iv.setImageResource(R.drawable.ic_white_night_bright)
                "Clouds" -> iv.setImageResource(R.drawable.ic_white_night_cloudy)
                "Thunderstorm" -> iv.setImageResource(R.drawable.ic_white_night_thunder)
                "Drizzle" -> iv.setImageResource(R.drawable.ic_white_night_rain)
                "Rain" -> iv.setImageResource(R.drawable.ic_white_night_shower)
            }
        } else {
            when (weatherType) {
                "Clear" -> iv.setImageResource(R.drawable.ic_white_day_bright)
                "Clouds" -> iv.setImageResource(R.drawable.ic_white_day_cloudy)
                "Thunderstorm" -> iv.setImageResource(R.drawable.ic_white_day_thunder)
                "Drizzle" -> iv.setImageResource(R.drawable.ic_white_day_rain)
                "Rain" -> iv.setImageResource(R.drawable.ic_white_day_shower)
            }
        }
    }

    fun getMaxMinTemp(list: ArrayList<X>, date: Int, isMax: Boolean): Int {

        var temp: Double
        if (isMax) {
            temp = -99.99
            list.forEach {
                Log.e(
                    "WatherKt",
                    "date " + date + " getDateFromTimeStamp(it.dt) " + getDateFromTimeStamp(it.dt)
                )
                if (getDateFromTimeStamp(it.dt) == date) {
                    if (it.main.temp_max > temp) {
                        temp = it.main.temp_max
                    }
                }
            }
        } else {
            temp = 99.99
            list.forEach {
                if (getDateFromTimeStamp(it.dt) == date) {
                    if (it.main.temp_min < temp) {
                        temp = it.main.temp_min
                    }
                }
            }
        }
        return temp.roundToInt()
    }

    fun getCurrentDate(): Int {
        val dateFormat = SimpleDateFormat("dd")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val today: Date = Calendar.getInstance().time
        Log.e("WeatherKt", "TODAY " + dateFormat.format(today).toInt())
        return dateFormat.format(today).toInt()
    }

    fun getWindDirection(degree: Int, iv: ImageView) {

        if (degree > 337.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_n)
            return
        }
        if (degree > 292.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_wn)
            return
        }
        if (degree > 247.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_w)
            return
        }
        if (degree > 202.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_ws)
            return
        }
        if (degree > 157.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_s)
            return
        }
        if (degree > 122.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_se)
            return
        }
        if (degree > 67.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_e)
            return
        }
        if (degree > 22.5) {
            iv.setImageResource(R.drawable.ic_icon_wind_ne)
            return
        }
        iv.setImageResource(R.drawable.ic_icon_wind_n)
        return
    }
}