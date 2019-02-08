package me.example.vacancies.appplication

import android.app.Application
import me.example.vacancies.repository.Resolver

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Resolver.create()
    }
}