package me.example.vacancies.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
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
        var searchTerm: String = ""
        val instance by lazy { VacancyRepository() }
    }

    @Inject
    lateinit var service: VacancyService
    @Inject
    lateinit var database: VacancyDatabase

    private lateinit var pagedListSource: LiveData<PagedList<Vacancy>>

    init {
        Resolver.serviceComponent.inject(this)
    }

    fun getVacancy(search: String) : LiveData<PagedList<Vacancy>> {
        searchTerm = search
        if(!::pagedListSource.isInitialized) {
            pagedListSource = LivePagedListBuilder(VacancyDataSourceFactory(database, service), getConfig())
                .build()
        }
        return pagedListSource
    }

    private fun getConfig() : PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(Constants.PageSize)
            .setMaxSize(PagedList.Config.MAX_SIZE_UNBOUNDED)
            .setPageSize(Constants.PageSize)
            .setPrefetchDistance(Constants.PrefetchDistance)
            .build()
    }

    class VacancyDataSourceFactory(val database: VacancyDatabase,
                                   val service: VacancyService): DataSource.Factory<Int, Vacancy>() {
        override fun create(): DataSource<Int, Vacancy> {
            return object : PageKeyedDataSource<Int, Vacancy>() {
                override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Vacancy>) {
                    loadPage(searchTerm, 0, object : ResponseCallback {
                        override fun handleResponse(resp: List<Vacancy>) {
                            callback.onResult(resp, null, 1)
                        }
                    })
                }

                override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {
                    loadPage(searchTerm, params.key, object : ResponseCallback {
                        override fun handleResponse(resp: List<Vacancy>) {
                            callback.onResult(resp, params.key+1)
                        }
                    })
                }

                override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Vacancy>) {
                    loadPage(searchTerm, params.key, object : ResponseCallback {
                        override fun handleResponse(resp: List<Vacancy>) {
                            callback.onResult(resp, params.key-1)
                        }
                    })
                }

            }
        }

        interface ResponseCallback {
            fun handleResponse(resp: List<Vacancy>)
        }

        private fun loadPage(searchTerm: String, page: Int, callback: ResponseCallback) {
            service.getVacancy(searchTerm, page).enqueue(object : Callback<List<Vacancy>?> {
                override fun onFailure(call: Call<List<Vacancy>?>, t: Throwable) {
                    EventBus.getDefault().post(LoadingFailureEvent(t.localizedMessage))
                    returnDatabaseResult(callback, searchTerm, page)
                }

                override fun onResponse(call: Call<List<Vacancy>?>, response: Response<List<Vacancy>?>) {
                    val body = response.body()
                    if(body == null) {
                        EventBus.getDefault().post(LoadingFailureEvent("null response"))
                        returnDatabaseResult(callback, searchTerm, page)
                        return
                    }
                    Executors.newSingleThreadExecutor().execute {
                        val data2save = body.map {
                            val modified = it
                            modified.page = page
                            return@map modified
                        }
                        database.vacancyDao().save(data2save)
                        returnDatabaseResult(callback, searchTerm, page)
                    }
                }

            })
        }

        private fun returnDatabaseResult(
            callback: ResponseCallback,
            searchTerm: String,
            page: Int
        ) {
            callback.handleResponse(
                if (searchTerm.isEmpty())
                    database.vacancyDao().getByPage(page)
                else
                    database.vacancyDao().getByQuery(searchTerm, page)
            )
        }
    }

}