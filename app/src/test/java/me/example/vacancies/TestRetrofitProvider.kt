package me.example.vacancies

import me.example.vacancies.models.Constants
import me.example.vacancies.repository.network.RetrofitProvider
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TestRetrofitProvider : RetrofitProvider {
    override fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BaseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}