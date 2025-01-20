package com.example.weathercast


import android.os.Bundle
import android.service.notification.Condition
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weathercast.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//756a6b4bbd2556eb1528bdde651e4402
class MainActivity : AppCompatActivity() {

    //TO FETCH THE VIEW ID WE USING VIEW BINDING
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Fetching Weather Data from default city
        fetchWeatherData("Udupi")


        //SEARCH FUNCTIONALITY FOR CITY WEATHER
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query.trim())
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ApiInterface::class.java)

        //CALLING GET WEATHER DATA FROM  API INTERFACE
        val response =
            retrofit.getWeatherData(cityName, "756a6b4bbd2556eb1528bdde651e4402", "metric")
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(
                call: Call<WeatherApp>,
                response: Response<WeatherApp>,
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    //update UI using  weather dta
                    updateUI(responseBody, cityName)
                }

            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun updateUI(weather: WeatherApp, cityName: String) {
        val temperature = weather.main.temp.toString()
        val humidity = weather.main.humidity
        val windSpeed = weather.wind.speed
        val sunRise = weather.sys.sunrise.toLong()
        val sunSet = weather.sys.sunset.toLong()
        val sea = weather.main.pressure
        val condition = weather.weather.firstOrNull()?.main ?: "unknown"
        val maxTemp = weather.main.temp_max
        val minTemp = weather.main.temp_min
        //Log.d("TAG", "onResponse: $temperature°C")
        binding.temp?.text = "$temperature°C"
        binding.weather.text = condition
        binding.maxTemp?.text = "Max Temp: $maxTemp °C"
        binding.minTemp?.text = "Min Temp: $minTemp °C "
        binding.humidity?.text = "$humidity %"
        binding.wind?.text = "$windSpeed m/s"
        binding.sunRise?.text = "${time(sunRise)}"
        binding.sunset?.text = "${time(sunSet)}"
        binding.seaLevel?.text = "$sea hpa"
        binding.conditions?.text = condition
        binding.date.text = dayName(System.currentTimeMillis())
        binding.day.text = date()
        binding.cityName.text = "$cityName"

        changeImageAccordingToWeatherCondition(condition)

    }

    private fun changeImageAccordingToWeatherCondition(condition:String) {
        when (condition)
        {
            "Clear Sky","Sunny","Clear" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }
            "Partly Clouds","Clouds","OverCast","Mist","Foggy" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }
            "Light Rain","Drizzle","Moderate  Rain","Showers","Heavy Rain" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

        }

        binding.lottieAnimationView.playAnimation()
    }

    }


   




private  fun date() : String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

private  fun  time(timestamp: Long) : String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format((Date(timestamp*1000)))
}
          private fun dayName(timestamp: Long) : String {
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            return sdf.format((Date()))
        }









