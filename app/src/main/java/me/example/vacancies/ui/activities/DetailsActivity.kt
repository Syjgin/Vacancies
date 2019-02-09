package me.example.vacancies.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*
import me.example.vacancies.R
import me.example.vacancies.models.Constants
import me.example.vacancies.models.Vacancy
import me.example.vacancies.viewmodel.VacancyDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getSerializableExtra(Constants.VacancyInfoKey) as Vacancy
        displayData(data)
    }

    private fun displayData(data: Vacancy) {
        vacancyTitle.text = data.title
        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val internalDate = dateFormat.parse(data.created_at)
        val outputFormat = SimpleDateFormat("dd.MM.YYYY", Locale.getDefault())
        createdAt.text = String.format(getString(R.string.date_pattern), outputFormat.format(internalDate))
        vacancyType.text = String.format(getString(R.string.type_pattern), data.type)
        url.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(data.url)
            startActivity(intent)
        }
        company.text = data.company
        companyUrl.text = data.company_url
        Glide.with(this).load(data.company_logo).into(image)
        location.text = data.location
        description.loadData(data.description, "text/html", "UTF-8")
        howto.loadData(data.how_to_apply,"text/html", "UTF-8")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val model = ViewModelProviders.of(this).get(VacancyDetailViewModel::class.java)
        model.scrollOffset = mainScroll.scrollY
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val model = ViewModelProviders.of(this).get(VacancyDetailViewModel::class.java)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            mainScroll.scrollTo(0, model.scrollOffset)
        }, Constants.LongUpdateTimeout)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }
}
