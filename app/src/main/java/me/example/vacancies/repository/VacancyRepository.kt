package me.example.vacancies.repository

import androidx.lifecycle.LiveData
import me.example.vacancies.models.LoadingFailureEvent
import me.example.vacancies.models.Vacancy
import me.example.vacancies.repository.db.VacancyDatabase
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import javax.inject.Inject

class VacancyRepository {

    companion object {
        val instance by lazy { VacancyRepository() }
    }

    @Inject
    lateinit var service: VacancyService
    @Inject
    lateinit var database: VacancyDatabase

    init {
        Resolver.serviceComponent.inject(this)
    }

    fun getVacancy(searchTerm: String, page: Int) : LiveData<List<Vacancy>> {
        service.getVacancy(searchTerm, page).enqueue(object : Callback<List<Vacancy>?> {
            override fun onFailure(call: Call<List<Vacancy>?>, t: Throwable) {
                EventBus.getDefault().post(LoadingFailureEvent(t.localizedMessage))
            }

            override fun onResponse(call: Call<List<Vacancy>?>, response: Response<List<Vacancy>?>) {
                val body = response.body()
                if(body == null) {
                    EventBus.getDefault().post(LoadingFailureEvent("null response"))
                    return
                }
                Executors.newSingleThreadExecutor().execute {
                    database.vacancyDao().save(body)
                }
            }

        })
        return if(searchTerm.isEmpty()) database.vacancyDao().getAll() else database.vacancyDao().getByQuery(searchTerm)
    }
}