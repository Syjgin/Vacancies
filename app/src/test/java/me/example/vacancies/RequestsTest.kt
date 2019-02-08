package me.example.vacancies

import me.example.vacancies.models.Vacancy
import me.example.vacancies.repository.VacancyService
import me.example.vacancies.repository.network.ServiceModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RequestsTest {
    private lateinit var testServiceComponent: TestServiceComponent
    @Inject
    lateinit var service: VacancyService

    private val countDownLatch = CountDownLatch(1)

    @Before
    fun prepare() {
        testServiceComponent = DaggerTestServiceComponent.builder().serviceModule(ServiceModule(TestRetrofitProvider())).build()
        testServiceComponent.inject(this)
    }

    @Test
    fun serviceRequest() {
        var receivedResponse: List<Vacancy>? = null
        service.getVacancy("", 0).enqueue(object : Callback<List<Vacancy>> {
            override fun onFailure(call: Call<List<Vacancy>>, t: Throwable) {
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call<List<Vacancy>>, response: Response<List<Vacancy>>) {
                receivedResponse = response.body()
                countDownLatch.countDown()
            }

        })
        try {
            countDownLatch.await(5, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Assert.assertNotNull(receivedResponse)
    }
}
