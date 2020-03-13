package app.android.weatherkt.controller

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.android.weatherkt.model.WeatherResponse
import app.android.weatherkt.model.X
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherViewModel : ViewModel() {

    var weatherList = MutableLiveData<ArrayList<X>>()
    var disposable: Disposable? = null
    val weatherRepository = WeatherRepository()
//    val context: Context = Application().applicationContext


    init {
//        getWeather(location)


    }

    fun getWeather(location: Location) {

    }

    fun getWeather(city: String) {
        disposable = weatherRepository.getWeather(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it: WeatherResponse) {
                weatherList.value = ArrayList(it.list)

//                val calendar = Calendar.getInstance()

                it.list.forEach {
                    //                    calendar.time = Date(it.dt.toLong() * 1000)

                    val simpleDataFormat = SimpleDateFormat("dd/EE")
                    val dateString = simpleDataFormat.format(Date(it.dt.toLong() * 1000))


                    Log.e("WeatherKt", dateString)

/*                    Log.e(
                        "WeatherKt",
                        "day: " + calendar.get(Calendar.DAY_OF_MONTH).toString() + " time: " + calendar.get(
                            Calendar.DAY_OF_WEEK
                        ).toString()
                    )*/


                }


/*                calendar.time = Date(it.list[0].dt.toLong() * 1000)
                Log.e("WeatherKt", calendar.get(Calendar.DAY_OF_MONTH).toString())*/

            }, fun(throwable: Throwable) {
                Log.e("WeatherKt", "onError $throwable")
                throwable.stackTrace
            })

//        Log.e("WeatherKt", "weatherList $weatherList")
    }

    override fun onCleared() {
        super.onCleared()

        disposable?.dispose()
    }
}