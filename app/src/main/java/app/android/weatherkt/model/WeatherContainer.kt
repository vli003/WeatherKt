package app.android.weatherkt.model

data class WeatherContainer(
    var dayOfWeek: String,
    var date: Int,
    var month: String,
    var tempMax: Double,
    var tempMin: Double,
    var humidity: Int,
    var windSpeed: Double,
    var windDeg: Int,
    var weatherType: String

)