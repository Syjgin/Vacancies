package me.example.vacancies.repository.network

import dagger.Component
import me.example.vacancies.repository.VacancyRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(subject: VacancyRepository)
}