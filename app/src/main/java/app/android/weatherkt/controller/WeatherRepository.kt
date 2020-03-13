package app.android.weatherkt.controller

import app.android.weatherkt.BuildConfig
import app.android.weatherkt.model.WeatherResponse
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform.Companion.INFO
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private var retrofit: Retrofit? = null
    private val openWeatherApi: OpenWeatherApi
    private val baseUrl = "https://api.openweathermap.org/data/2.5/"

    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                val client = OkHttpClient.Builder()
                    .addInterceptor(
                        LoggingInterceptor.Builder()
                            .loggable(BuildConfig.DEBUG)
                            .setLevel(Level.BODY)
                            .log(INFO)
                            .tag("WeatherApi")
                            .build()
                    ).build()

                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    init {
        openWeatherApi = retrofitInstance.create(OpenWeatherApi::class.java)
    }

    fun getWeather(city: String): Single<WeatherResponse> {
        return openWeatherApi.request(city)
    }
}