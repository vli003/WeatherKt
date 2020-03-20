package app.android.weatherkt.controller

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.android.weatherkt.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment(R.layout.fragment_main), View.OnClickListener {

    private var weatherByHoursAdapter: WeatherByHoursAdapter? = null
    private var weatherByDaysAdapter: WeatherByDaysAdapter? = null
    private lateinit var viewModel: WeatherViewModel
    private var disposable: Disposable? = null
    private val weatherController: WeatherController = WeatherController()
    private lateinit var txt: String
    private val degree = "\u00B0"
    private val windSpeed = "m/sec"
    private var fusedLocationClient: FusedLocationProviderClient? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        iv_current_loc.setOnClickListener(this)
        iv_search.setOnClickListener(this)

        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        viewModel.txtLiveData.observe(viewLifecycleOwner,
            Observer { txt ->
                txt?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            })

        viewModel.currentWeatherLiveData.observe(viewLifecycleOwner,
            Observer { currentWeather ->
                tv_city.text = currentWeather.name
                tv_day_date_month.text =
                    weatherController.parseTimeStamp(currentWeather.dt, "EE, dd MMMM")
                weatherController.weatherIcon(
                    currentWeather.weather[0].main,
                    iv_big_icon,
                    currentWeather.dt
                )

                txt = currentWeather.main.temp_max.toInt().toString() + degree
                tv_temp_max.text = txt

                txt = currentWeather.main.temp_min.toInt().toString() + degree
                tv_temp_min.text = txt

                tv_humidity.text = currentWeather.main.humidity.toString()

                txt = currentWeather.wind.speed.roundToInt().toString() + windSpeed
                tv_wind.text = txt

                weatherController.getWindDirection(
                    currentWeather.wind.deg,
                    iv_wind_dir
                )
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

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(activity!!.applicationContext)

        val rxPermissions = RxPermissions(this)
        disposable = rxPermissions
            .request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe { granted: Boolean ->
                if (granted) {
                    getLastLocation()
                } else {
                    Log.e("WeatherKt", "granted = false")
                }
            }
    }

/*    override fun onStart() {
        super.onStart()

        getLastLocation()
    }*/

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
            location?.let { it: Location ->
                Log.e("WeatherKt", "latitude: ${it.latitude} longitude: ${it.longitude}")
                viewModel.getCurrentWeather(it)
                viewModel.getHourlyForecast(it)
            } ?: kotlin.run {
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_current_loc -> getLastLocation()

        }
    }
}
