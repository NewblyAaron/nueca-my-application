package me.newbly.myapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.newbly.myapplication.models.AnimeData
import me.newbly.myapplication.models.JikanDataModel
import me.newbly.myapplication.repositories.JikanRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimeListViewModel : ViewModel() {
    private var animeListLiveData = MutableLiveData<List<AnimeData>>()

    fun getAnimeList() {
        JikanRepository.RetrofitInstance.api.getAnimes().enqueue(object: Callback<JikanDataModel> {
            override fun onResponse(call: Call<JikanDataModel>, response: Response<JikanDataModel>) {
                if (response.body() != null) {
                    animeListLiveData.value = response.body()!!.data
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<JikanDataModel>, t: Throwable) {
                Log.d("JikanApi", t.message.toString())
            }
        })
    }

    fun observeAnimeListLiveData(): LiveData<List<AnimeData>> {
        return animeListLiveData
    }
}