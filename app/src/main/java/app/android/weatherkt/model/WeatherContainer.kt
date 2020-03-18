package app.android.weatherkt.model

data class WeatherContainer(
    val dayOfWeek: String,
    val date: Int,
    val month: String,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val weatherType: String

)