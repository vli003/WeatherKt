package app.android.weatherkt.controller

import app.android.weatherkt.model.CurrentWeatherResponse
import app.android.weatherkt.model.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("forecast?appid=4dfa9919178029ccc88dedab96518615&units=metric")
    fun getHourlyForecast(@Query("lat") lat: Double,
                          @Query("lon") lon: Double): Single<WeatherResponse>

    @GET("weather?appid=4dfa9919178029ccc88dedab96518615&units=metric")
    fun getCurrentWeather(@Query("lat") lat: Double,
                          @Query("lon") lon: Double): Single<CurrentWeatherResponse>
}