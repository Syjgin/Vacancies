package me.example.vacancies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.example.vacancies.models.Vacancy
import me.example.vacancies.repository.VacancyRepository

class VacancyListViewModel : ViewModel() {
    private lateinit var data: LiveData<List<Vacancy>>

    fun getVacancyList(searchTerm: String, page: Int): LiveData<List<Vacancy>> {
        data = VacancyRepository.instance.getVacancy(searchTerm, page)
        return data
    }
}