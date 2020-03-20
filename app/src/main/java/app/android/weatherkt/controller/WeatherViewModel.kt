package app.android.weatherkt.controller

import android.location.Location
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

class WeatherViewModel : ViewModel() {

    var txtLiveData = MutableLiveData<String>()
    var currentWeatherLiveData = MutableLiveData<CurrentWeatherResponse>()
    var weatherListByHoursLiveData = MutableLiveData<ArrayList<X>>()
    var weatherListByDaysLiveData = MutableLiveData<ArrayList<WeatherContainer>>()
    private var disposable: Disposable? = null
    private val weatherRepository = WeatherRepository()
    private val wController: WeatherController = WeatherController()


    fun getCurrentWeather(location: Location) {
        if (disposable != null) {
            disposable?.dispose()
        }
        disposable = weatherRepository.getCurrentWeather(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(it: CurrentWeatherResponse) {
                currentWeatherLiveData.value = it
            }, fun(throwable: Throwable) {
                Log.e("WeatherKt", "getCurrentWeather onError: ${throwable.message}")
                throwable.stackTrace
                txtLiveData.value = throwable.message
            })
    }

    fun getHourlyForecast(location: Location) {
/*        if (disposable != null) {
            disposable?.dispose()
        }*/

        disposable = weatherRepository.getHourlyForecast(location.latitude, location.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(fun(response: WeatherResponse) {
                weatherListByHoursLiveData.value = ArrayList(response.list.subList(0, 10))

                val list: ArrayList<WeatherContainer> = ArrayList()
                val wc = wController.createWeatherContainer()
                var day = 0
                var count = 0

                response.list.forEach {
                    if (wController.parseTimeStamp(it.dt, "dd").toInt() == wc.date) {
                        wController.getMaxMinValues(it, wc)
                        wc.dayOfWeek = wController.parseTimeStamp(it.dt, "EE")

                        if (count == response.list.size - 1) {
                            wController.addWeatherContainerToList(wc, list, it)
                        }
                    } else {
                        if (wc.tempMax != -99.99) {
                            wController.addWeatherContainerToList(wc, list, it)
                        }
                        day++
                        wController.resetWeatherContainer(wc, day)
                    }
                    count++
                }
                weatherListByDaysLiveData.value = list
            }, fun(throwable: Throwable) {
                Log.e("WeatherKt", "getHourlyForecast onError: ${throwable.message}")
                throwable.stackTrace
                txtLiveData.value = throwable.message
            })
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.dispose()
        }
    }
}