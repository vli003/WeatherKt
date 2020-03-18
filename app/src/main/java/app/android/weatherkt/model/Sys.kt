package app.android.weatherkt.model

data class Sys(
    val pod: String,
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)