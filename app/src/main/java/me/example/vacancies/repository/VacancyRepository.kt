package me.example.vacancies.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import me.example.vacancies.models.Constants
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

    fun getVacancy(searchTerm: String) : LiveData<PagedList<Vacancy>> {

        return LivePagedListBuilder(if(searchTerm.isEmpty()) database.vacancyDao().getByPage() else database.vacancyDao().getByQuery(searchTerm), getConfig())
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Vacancy>() {
                override fun onZeroItemsLoaded() {
                    loadPage(searchTerm, 0)
                }

                override fun onItemAtEndLoaded(itemAtEnd: Vacancy) {
                    val newPage = itemAtEnd.page!!+1
                    loadPage(searchTerm, newPage)
                }

                override fun onItemAtFrontLoaded(itemAtFront: Vacancy) {
                    val newPage = itemAtFront.page!!-1
                    if(newPage > 0) {
                        loadPage(searchTerm, newPage)
                    }
                }
            })
            .build()
    }

    private fun getConfig() : PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(Constants.PageSize)
            .setMaxSize(PagedList.Config.MAX_SIZE_UNBOUNDED)
            .setPageSize(Constants.PageSize)
            .setPrefetchDistance(5)
            .build()
    }

    private fun loadPage(searchTerm: String, page: Int) {
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
                    val data2save = body.map {
                        val modified = it
                        modified.page = page
                        return@map modified
                    }
                    database.vacancyDao().save(data2save)
                }
            }

        })
    }
}