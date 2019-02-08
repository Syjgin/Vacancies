package me.example.vacancies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.example.vacancies.models.LoadingFailureEvent
import me.example.vacancies.models.Vacancy
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class VacancyRepository {

    companion object {
        val instance by lazy { VacancyRepository() }
    }

    @Inject
    lateinit var service: VacancyService

    init {
        Resolver.serviceComponent.inject(this)
    }

    fun getVacancy(searchTerm: String, page: Int) : LiveData<List<Vacancy>?> {
        val data = MutableLiveData<List<Vacancy>>()
        service.getVacancy(searchTerm, page).enqueue(object : Callback<List<Vacancy>?> {
            override fun onFailure(call: Call<List<Vacancy>?>, t: Throwable) {
                EventBus.getDefault().post(LoadingFailureEvent(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<Vacancy>?>, response: Response<List<Vacancy>?>) {
                if(response.body() == null) {
                    EventBus.getDefault().post(LoadingFailureEvent("null response"))
                    return
                }
                data.value = response.body()
            }

        })
        return data
    }
}