package me.newbly.myapplication

import android.app.Application
import me.newbly.myapplication.api.JikanService

open class MyApplication : Application() {
    lateinit var service: JikanService

    override fun onCreate() {
        super.onCreate()

        service = JikanService.create()
    }
}