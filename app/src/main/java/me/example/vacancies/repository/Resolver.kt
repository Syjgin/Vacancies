package me.example.vacancies.repository

import me.example.vacancies.repository.network.DaggerServiceComponent
import me.example.vacancies.repository.network.ServiceComponent
import me.example.vacancies.repository.network.ServiceModule

object Resolver {
    lateinit var serviceComponent: ServiceComponent
    private set

    fun create() {
        serviceComponent = DaggerServiceComponent.builder().serviceModule(ServiceModule()).build()
    }
}