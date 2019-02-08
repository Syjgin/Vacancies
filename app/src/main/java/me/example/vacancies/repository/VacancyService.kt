package me.example.vacancies.repository

import me.example.vacancies.models.Vacancy
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VacancyService {
    @GET("positions.json")
    fun getVacancy(
        @Query("search") speciality: String,
        @Query("page") page: Int) : Call<List<Vacancy>>
}