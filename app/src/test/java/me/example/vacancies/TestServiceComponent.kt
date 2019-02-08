package me.example.vacancies

import dagger.Component
import me.example.vacancies.repository.network.ServiceModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class])
interface TestServiceComponent {
    fun inject(subject: RequestsTest)
}