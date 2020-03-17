package app.android.weatherkt.controller

import android.location.Location
import android.util.Log
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

    var weatherList = ArrayList<X>()
    var currentWeatherLiveData = MutableLiveData<WeatherResponse>()
    var weatherListByHoursLiveData = MutableLiveData<ArrayList<X>>()
    var weatherListByDaysLiveData = MutableLiveData<ArrayList<X>>()
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
                weatherList = ArrayList(it.list)
//                weatherList = it.list as ArrayList<X>
                currentWeatherLiveData.value = it

//                val calendar = Calendar.getInstance()

                it.list.forEach {
                    //                    calendar.time = Date(it.dt.toLong() * 1000)
                    val simpleDataFormat = SimpleDateFormat("EE/dd/MMMM/HH/mm")
                    val dateString = simpleDataFormat.format(Date(it.dt.toLong() * 1000))
                    Log.e("WeatherKt", dateString)
/*                    Log.e(
                        "WeatherKt",
                        "day: " + calendar.get(Calendar.DAY_OF_MONTH).toString() + " time: " + calendar.get(
                            Calendar.DAY_OF_WEEK
                        ).toString()
                    )*/
                }

//                weatherListByHoursLiveData.value = ArrayList(weatherList.takeLast(10))
                weatherListByHoursLiveData.value = ArrayList(weatherList.subList(0, 10))
                Log.e(
                    "WeatherKt",
                    "weatherListByHours.size: " + weatherListByHoursLiveData.value?.size.toString()
                )

/*                calendar.time = Date(it.list[0].dt.toLong() * 1000)
                Log.e("WeatherKt", calendar.get(Calendar.DAY_OF_MONTH).toString())*/

            }, fun(throwable: Throwable) {
                Log.e("WeatherKt", "onError $throwable")
                throwable.stackTrace
            })
    }

    override fun onCleared() {
        super.onCleared()

        disposable?.dispose()
    }
}