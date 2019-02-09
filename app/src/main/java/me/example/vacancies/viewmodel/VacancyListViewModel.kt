package me.example.vacancies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import me.example.vacancies.models.Vacancy
import me.example.vacancies.repository.VacancyRepository

class VacancyListViewModel : ViewModel() {
    var scrollPosition = 0
        private set
    companion object {
        var searchTerm: String = ""
        private set
    }

    fun getVacancyList(): LiveData<PagedList<Vacancy>> {
        return VacancyRepository.instance.getVacancy(searchTerm, searchTerm != VacancyRepository.cachedSearchTerm)
    }

    fun updateSearchTerm(newTerm: String) {
        searchTerm = newTerm
    }

    fun refreshScrollPosition(newPosition: Int) {
        if(newPosition <= 0)
            return
        scrollPosition = newPosition
    }
}