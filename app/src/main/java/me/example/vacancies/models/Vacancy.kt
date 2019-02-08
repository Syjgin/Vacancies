package me.example.vacancies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Vacancy(
    @PrimaryKey
    val id: String,
    val type: String,
    val url: String,
    val created_at: String,
    val company: String,
    val company_url: String?,
    val location: String,
    val title: String,
    val description: String,
    val how_to_apply: String,
    val company_logo: String?,
    var page: Int?) : Serializable