package me.example.vacancies.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import me.example.vacancies.R
import me.example.vacancies.models.Constants
import me.example.vacancies.models.LoadingFailureEvent
import me.example.vacancies.models.OpenDetailsEvent
import me.example.vacancies.models.Vacancy
import me.example.vacancies.ui.adapters.VacancyListAdapter
import me.example.vacancies.utils.EventBusUtils
import me.example.vacancies.viewmodel.VacancyListViewModel
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    private val adapter = VacancyListAdapter()
    private var layoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBusUtils.registerIfNeeded(this)
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mainList.layoutManager = layoutManager
        mainList.adapter = adapter
        val model = ViewModelProviders.of(this).get(VacancyListViewModel::class.java)
        mainList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(layoutManager != null) {
                    val position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    model.refreshScrollPosition(position)
                }
            }
        })
        model.getVacancyList("").observe(this,
            Observer<PagedList<Vacancy>> { t ->
                adapter.submitList(t)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    layoutManager?.scrollToPosition(model.scrollPosition)
                }, Constants.UpdateTimeout)
            })
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregisterIfNeeded(this)
    }

    @Subscribe
    fun onLoadingFailure(event: LoadingFailureEvent) {
        Toast.makeText(this, getString(R.string.loading_failure), Toast.LENGTH_LONG).show()
    }

    @Subscribe
    fun onDetailsOpening(event: OpenDetailsEvent) {
        val vacancy = event.vacancy
        val bundle = Bundle()
        bundle.putSerializable(Constants.VacancyInfoKey, vacancy)
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
