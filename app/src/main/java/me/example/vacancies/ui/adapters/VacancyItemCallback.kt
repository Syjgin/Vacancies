package me.example.vacancies.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import me.example.vacancies.models.Vacancy

class VacancyItemCallback : DiffUtil.ItemCallback<Vacancy>() {
    override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem.type == newItem.type &&
                oldItem.url == newItem.url &&
                oldItem.created_at == newItem.created_at &&
                oldItem.company == newItem.company &&
                oldItem.company_url == newItem.company_url &&
                oldItem.location == newItem.location &&
                oldItem.title == newItem.title &&
                oldItem.description == newItem.description &&
                oldItem.how_to_apply == newItem.how_to_apply &&
                oldItem.company_logo == newItem.company_logo
    }
}