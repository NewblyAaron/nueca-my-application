package me.newbly.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Converter
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jaxb.JaxbConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.gridRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val api =
            Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JikanService.Jikan::class.java)

        api.getAnimes().enqueue(object : Callback<JikanService.Anime> {
            override fun onResponse(
                p0: Call<JikanService.Anime>,
                p1: Response<JikanService.Anime>
            ) {
                if (p1.isSuccessful) {
                    p1.body()?.let { body ->
                        val adapter = GridViewAdapter(body.data)
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(p0: Call<JikanService.Anime>, p1: Throwable) {
                Log.i("JIKAN", "onFailure: ${p1.message}")
            }
        })
    }
}