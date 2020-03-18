package app.android.weatherkt.controller

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.android.weatherkt.model.CurrentWeatherResponse
import app.android.weatherkt.model.WeatherContainer
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
    var currentWeatherLiveData = MutableLiveData<CurrentWeatherResponse>()
    var weatherListByHoursLiveData = MutableLiveData<ArrayList<X>>()
    var weatherListByDaysLiveData = MutableLiveData<ArrayList<WeatherContainer>>()
    var disposable: Disposable? = null
    val weatherRepository = WeatherRepository()
//    val weatherController: WeatherController = WeatherController()
//    val context: Context = Application().applicationContext


    init {
//        getWeather(location)


    }

/*    fun getWeather(location: Location) {

    }*/

    fun getCurrentWeather(city: String) {
        if (disposable != null) {
            disposable?.dispose()
        }

        disposable = weatherRepository.getCurrentWeather(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it: CurrentWeatherResponse) {
                currentWeatherLiveData.value = it

//                Log.e("WeatherKt", "CurrentWeatherResponse $it")

            }, fun(throwable: Throwable) {
                Log.e("WeatherKt", "onError $throwable")
                throwable.stackTrace
            })
    }

    fun getHourlyForecast(city: String) {
/*        if (disposable != null) {
            disposable?.dispose()
        }*/

        disposable = weatherRepository.getHourlyForecast(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it2: WeatherResponse) {
                weatherList = ArrayList(it2.list)
//                weatherList = it.list as ArrayList<X>


//                val calendar = Calendar.getInstance()


                var list: ArrayList<WeatherContainer> = ArrayList()
                var dayOfWeek = "Monday"
                var maxTemp = -99.99
                var minTemp = 99.99
                var humidity = -1
                var windSpeed = -1.0
                var day = 0
                var count = 0
                val calendar = Calendar.getInstance()
                var date = calendar.get(Calendar.DAY_OF_MONTH)
                var month = SimpleDateFormat("MMMM").format(calendar.time)

                val simpleDataFormat = SimpleDateFormat("dd")
                val simpleDataFormat2 = SimpleDateFormat("EE")
                val simpleDataFormat3 = SimpleDateFormat("dd MMMM")
//                    val simpleDataFormat = SimpleDateFormat("EE/dd/MMMM/HH/mm/aa")
                var dateString: String

                it2.list.forEach {
                    //                    calendar.time = Date(it.dt.toLong() * 1000)
                    dateString = simpleDataFormat.format(Date(it.dt.toLong() * 1000))

                    Log.e("WeatherKt", dateString)
/*                    Log.e(
                        "WeatherKt",
                        "day: " + calendar.get(Calendar.DAY_OF_MONTH).toString() + " time: " + calendar.get(
                            Calendar.DAY_OF_WEEK
                        ).toString()
                    )*/



                    if (dateString.toInt() == date) {
                        Log.e("WeatherKt", "dateString: $dateString date $date")
                        if (it.main.temp_max > maxTemp) maxTemp = it.main.temp_max
                        if (it.main.temp_min < minTemp) minTemp = it.main.temp_min
                        if (it.main.humidity > humidity) humidity = it.main.humidity
                        if (it.wind.speed > windSpeed) windSpeed = it.wind.speed
                        dayOfWeek = simpleDataFormat2.format(Date(it.dt.toLong() * 1000))

                        if (count == it2.list.size - 1) {
                            val weatherContainer = WeatherContainer(
                                dayOfWeek,
                                date,
                                month.toString(),
                                maxTemp,
                                minTemp,
                                humidity,
                                windSpeed,
                                it.wind.deg,
                                it.weather[0].main
                            )
                            list.add(weatherContainer)

                            Log.e("WeatherKt", "dateString2: $dateString date2: $date")
                            Log.e("WeatherKt", "weatherContainer: $weatherContainer")
                        }
                    } else {

                        if (maxTemp != -99.99) {
                            val weatherContainer = WeatherContainer(
                                dayOfWeek,
                                date,
                                month.toString(),
                                maxTemp,
                                minTemp,
                                humidity,
                                windSpeed,
                                it.wind.deg,
                                it.weather[0].main
                            )
                            list.add(weatherContainer)

                            Log.e("WeatherKt", "dateString2: $dateString date2: $date")
                            Log.e("WeatherKt", "weatherContainer: $weatherContainer")
                        }

                        maxTemp = -99.99
                        minTemp = 99.99
                        humidity = -1
                        windSpeed = -1.0
                        day++
                        val dateDate = getDaysAgo(day)
                        date = dateDate.date
                    }
                    count++
                }

//                weatherListByHoursLiveData.value = ArrayList(weatherList.takeLast(10))
                weatherListByHoursLiveData.value = ArrayList(weatherList.subList(0, 10))
                weatherListByDaysLiveData.value = list

/*                calendar.time = Date(it.list[0].dt.toLong() * 1000)
                Log.e("WeatherKt", calendar.get(Calendar.DAY_OF_MONTH).toString())*/
            }, fun(throwable: Throwable) {
                Log.e("WeatherKt", "onError $throwable")
                throwable.stackTrace
            })
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.dispose()
        }
    }

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, +daysAgo)

        return calendar.time
    }
}