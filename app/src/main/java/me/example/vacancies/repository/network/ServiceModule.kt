package me.example.vacancies.repository.network

import dagger.Module
import dagger.Provides
import me.example.vacancies.models.Constants
import me.example.vacancies.repository.VacancyService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideService() : VacancyService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(VacancyService::class.java)
    }
}