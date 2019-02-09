package me.example.vacancies.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private var searchResultsMode = false
    private var openingDetailsInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val extras = intent.extras?.keySet()
        if(extras != null) {
            val userQuery = intent.extras.getString("query")
            if(userQuery != null) {
                title = userQuery
                searchResultsMode = true
            }
        }
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
        displaySearchResults(model)
        if(searchResultsMode) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun displaySearchResults(model: VacancyListViewModel) {
        model.getVacancyList().removeObservers(this)
        model.getVacancyList().observe(this,
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(searchResultsMode)
            return true
        val inflater = menuInflater
        inflater.inflate(R.menu.search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query == null)
                    return false
                val model = ViewModelProviders.of(this@MainActivity).get(VacancyListViewModel::class.java)
                model.updateSearchTerm(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    @Subscribe
    fun onLoadingFailure(event: LoadingFailureEvent) {
        Toast.makeText(this, getString(R.string.loading_failure), Toast.LENGTH_LONG).show()
    }

    @Subscribe
    fun onDetailsOpening(event: OpenDetailsEvent) {
        if(openingDetailsInProgress)
            return
        openingDetailsInProgress = true
        val vacancy = event.vacancy
        val bundle = Bundle()
        bundle.putSerializable(Constants.VacancyInfoKey, vacancy)
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val model = ViewModelProviders.of(this@MainActivity).get(VacancyListViewModel::class.java)
        model.updateSearchTerm("")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        openingDetailsInProgress = false
    }
}
