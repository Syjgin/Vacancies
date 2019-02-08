package me.example.vacancies.repository.network

import dagger.Module
import dagger.Provides
import me.example.vacancies.repository.VacancyService
import javax.inject.Singleton

@Module
class ServiceModule(private val provider: RetrofitProvider) {
    @Provides
    @Singleton
    fun provideService() : VacancyService {
        return provider.getRetrofit().create(VacancyService::class.java)
    }
}