package me.example.vacancies.repository

import android.content.Context
import me.example.vacancies.repository.db.DatabaseModule
import me.example.vacancies.repository.network.CachedRetrofitProvider
import me.example.vacancies.repository.network.DaggerServiceComponent
import me.example.vacancies.repository.network.ServiceComponent
import me.example.vacancies.repository.network.ServiceModule

object Resolver {
    lateinit var serviceComponent: ServiceComponent
    private set

    fun create(context: Context) {
        serviceComponent = DaggerServiceComponent.builder()
            .serviceModule(ServiceModule(CachedRetrofitProvider(context)))
            .databaseModule(DatabaseModule(context))
            .build()
    }
}