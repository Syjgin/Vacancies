package me.example.vacancies.repository.network

import dagger.Component
import me.example.vacancies.repository.VacancyRepository
import me.example.vacancies.repository.db.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class, DatabaseModule::class])
interface ServiceComponent {
    fun inject(subject: VacancyRepository)
}