package me.example.vacancies.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.example.vacancies.R
import me.example.vacancies.models.OpenDetailsEvent
import me.example.vacancies.models.Vacancy
import org.greenrobot.eventbus.EventBus

class VacancyListAdapter : PagedListAdapter<Vacancy, VacancyListAdapter.VacancyListItem>(VacancyItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyListItem {
        val mainView = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        val holder = VacancyListItem(mainView)
        holder.itemView.setOnClickListener {
            EventBus.getDefault().post(OpenDetailsEvent(getItem(holder.adapterPosition)!!))
        }
        return holder
    }

    override fun onBindViewHolder(holder: VacancyListItem, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.itemView.findViewById<TextView>(R.id.title).text = currentItem.title
        val imageView = holder.itemView.findViewById<ImageView>(R.id.image)
        if(currentItem.company_logo != null) {
            imageView.visibility = View.VISIBLE
            Glide.with(holder.itemView).load(currentItem.company_logo).into(imageView)
        } else {
            imageView.visibility = View.GONE
        }
        holder.itemView.findViewById<TextView>(R.id.city).text = currentItem.location
    }



    class VacancyListItem(itemView: View): RecyclerView.ViewHolder(itemView)
}