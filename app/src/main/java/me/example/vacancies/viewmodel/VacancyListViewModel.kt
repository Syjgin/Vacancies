package me.example.vacancies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import me.example.vacancies.models.Vacancy
import me.example.vacancies.repository.VacancyRepository

class VacancyListViewModel : ViewModel() {

    fun getVacancyList(searchTerm: String): LiveData<PagedList<Vacancy>> {
        return VacancyRepository.instance.getVacancy(searchTerm)
    }
}