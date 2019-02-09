package me.example.vacancies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import me.example.vacancies.models.Vacancy
import me.example.vacancies.repository.VacancyRepository

class VacancyListViewModel : ViewModel() {
    var scrollPosition = 0
        private set

    fun getVacancyList(searchTerm: String): LiveData<PagedList<Vacancy>> {
        return VacancyRepository.instance.getVacancy(searchTerm)
    }

    fun refreshScrollPosition(newPosition: Int) {
        if(newPosition <= 0)
            return
        scrollPosition = newPosition
    }
}