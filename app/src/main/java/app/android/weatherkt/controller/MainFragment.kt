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
        viewModel.weatherList.observe(
            viewLifecycleOwner,
            Observer { weatherList: List<X> -> weatherByHoursAdapter?.submitList(weatherList) })
        viewModel.weatherList.observe(
            viewLifecycleOwner,
            Observer { weatherList: List<X> -> weatherByDaysAdapter?.submitList(weatherList) })

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
}
