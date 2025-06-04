package com.example.investcitynfc

import android.app.Application

class InvestCityApp : Application() {
    companion object {
        lateinit var instance: InvestCityApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
} 