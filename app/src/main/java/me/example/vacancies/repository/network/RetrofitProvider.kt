package me.example.vacancies.repository.network

import retrofit2.Retrofit

interface RetrofitProvider {
    fun getRetrofit(): Retrofit
}