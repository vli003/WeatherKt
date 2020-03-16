package app.android.weatherkt.controller

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.weatherkt.R
import app.android.weatherkt.model.X
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    private var weatherByHoursAdapter: WeatherByHoursAdapter? = null
    private var weatherByDaysAdapter: WeatherByDaysAdapter? = null
    private lateinit var viewModel: WeatherViewModel
    //    private var rxPermissions: RxPermissions? = activity?.let { RxPermissions(it) }
    private var disposable: Disposable? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        viewModel.currentWeatherLiveData.observe(viewLifecycleOwner,
            Observer { currentWeather ->
                tv_city.text = currentWeather.city.name
                tv_day_date_month.text = parseTimeStamp(currentWeather.list[0].dt)
                weatherIcon(currentWeather.list[0].weather[0].main)

//                tv_temp_max.text = currentWeather.list[0].main.temp_max.toString() + "\u2103"
                tv_temp_max.text =
                    getMaxTemp(
                        currentWeather.list as ArrayList<X>,
                        getCurrentDate()
                    ).toString() + "\u2103"


//                tv_temp_min.text = currentWeather.list[0].main.temp_min.toString() + "\u2103"
                tv_temp_min.text =
                    getMinTemp(currentWeather.list, getCurrentDate()).toString() + "\u2103"

                tv_humidity.text = currentWeather.list[0].main.humidity.toString()
                tv_wind.text = currentWeather.list[0].wind.speed.toString() + " m / sec"

                getWindDirection(currentWeather.list[0].wind.deg)
            })

        viewModel.weatherListByHoursLiveData.observe(
            viewLifecycleOwner,
            Observer { weatherByHours -> weatherByHoursAdapter?.submitList(weatherByHours) })
        viewModel.weatherListByDaysLiveData.observe(
            viewLifecycleOwner,
            Observer { weatherByDays -> weatherByDaysAdapter?.submitList(weatherByDays) })

        weatherByHoursAdapter = WeatherByHoursAdapter()
        weatherByDaysAdapter = WeatherByDaysAdapter()
        rv_hours.adapter = weatherByHoursAdapter
        rv_hours.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rv_days.adapter = weatherByDaysAdapter
        rv_days.layoutManager = LinearLayoutManager(context)


        val rxPermissions = RxPermissions(this)


        disposable = rxPermissions
            .request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe { granted: Boolean ->
                if (granted) {
                    viewModel.getWeather("kyiv")

                } else {

                    Log.e("WeatherKt", "granted = false")
                }
            }
    }

    fun parseTimeStamp(dt: Int): String {
        val simpleDataFormat = SimpleDateFormat("EE, dd MMMM")
        return simpleDataFormat.format(Date(dt.toLong() * 1000))
    }

    fun getDateFromTimeStamp(dt: Int): Int {
        val simpleDataFormat = SimpleDateFormat("dd")
        return simpleDataFormat.format(Date(dt.toLong() * 1000)).toInt()
    }

    fun weatherIcon(weatherType: String) {
        when (weatherType) {
            "Clear" -> iv_big_icon.setImageResource(R.drawable.ic_white_day_bright)
            "Clouds" -> iv_big_icon.setImageResource(R.drawable.ic_white_day_cloudy)
        }
    }

    fun getMaxTemp(list: ArrayList<X>, date: Int): Int {
        var maxTemp = -99.99

        list.forEach {


            if (getDateFromTimeStamp(it.dt) == date) {
                if (it.main.temp_max > maxTemp) {
                    maxTemp = it.main.temp_max
                }
            }
        }
        return maxTemp.roundToInt()
    }

    fun getMinTemp(list: ArrayList<X>, date: Int): Int {
        var minTemp = 99.99

        list.forEach {


            if (getDateFromTimeStamp(it.dt) == date) {
                if (it.main.temp_min < minTemp) {
                    minTemp = it.main.temp_min
                }
            }
        }
        return minTemp.roundToInt()
    }

    fun getCurrentDate(): Int {
        val dateFormat = SimpleDateFormat("dd");
        dateFormat.timeZone = TimeZone.getTimeZone("UTC");
        val today: Date = Calendar.getInstance().time;

//        Log.e("WeatherKt", "TODAY " + dateFormat.format(today))

        return dateFormat.format(today).toInt()
    }

    fun getWindDirection(degree: Int) {

        if (degree > 337.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_n)
            return
        }
        if (degree > 292.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_wn)
            return
        }
        if (degree > 247.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_w)
            return
        }
        if (degree > 202.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_ws)
            return
        }
        if (degree > 157.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_s)
            return
        }
        if (degree > 122.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_se)
            return
        }
        if (degree > 67.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_e)
            return
        }
        if (degree > 22.5) {
            iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_ne)
            return
        }
        iv_wind_dir.setImageResource(R.drawable.ic_icon_wind_n)
        return
    }
}
