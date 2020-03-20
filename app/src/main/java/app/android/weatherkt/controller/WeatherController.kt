package app.android.weatherkt.controller

import android.util.Log
import android.widget.ImageView
import app.android.weatherkt.R
import app.android.weatherkt.model.WeatherContainer
import app.android.weatherkt.model.X
import java.text.SimpleDateFormat
import java.util.*

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

    fun parseTimeStamp(dt: Int, pattern: String): String {
        val simpleDataFormat = SimpleDateFormat(pattern)
        return simpleDataFormat.format(Date(dt.toLong() * 1000))
    }

    fun weatherIcon(weatherType: String, iv: ImageView, dt: Int) {
        val isNight: Boolean = if (dt == -1) false
        else parseTimeStamp(dt, "HH") in nightHours

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

    fun getMaxMinValues(x: X, wc: WeatherContainer) {
        if (x.main.temp_max > wc.tempMax) wc.tempMax = x.main.temp_max
        if (x.main.temp_min < wc.tempMin) wc.tempMin = x.main.temp_min
        if (x.main.humidity > wc.humidity) wc.humidity = x.main.humidity
        if (x.wind.speed > wc.windSpeed) wc.windSpeed = x.wind.speed
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

    fun getDaysInAdvance(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +daysAgo)
        return calendar.time
    }

    fun createWeatherContainer(): WeatherContainer {
        val calendar = Calendar.getInstance()
        return WeatherContainer(
            "Monday",
            calendar.get(Calendar.DAY_OF_MONTH),
            SimpleDateFormat("MMMM").format(calendar.time),
            -99.99,
            99.99,
            -1,
            -1.0,
            -1,
            "Rain"
        )
    }

    fun resetWeatherContainer(wc: WeatherContainer, day: Int) {
        wc.tempMax = -99.99
        wc.tempMin = 99.99
        wc.humidity = -1
        wc.windSpeed = -1.0
        wc.date = getDaysInAdvance(day).date
    }

    fun addWeatherContainerToList(wc: WeatherContainer, list: ArrayList<WeatherContainer>, x: X) {
        wc.windDeg = x.wind.deg
        wc.weatherType = x.weather[0].main
        list.add(
            WeatherContainer(
                wc.dayOfWeek,
                wc.date,
                wc.month,
                wc.tempMax,
                wc.tempMin,
                wc.humidity,
                wc.windSpeed,
                wc.windDeg,
                wc.weatherType
            )
        )
    }
}