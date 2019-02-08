package me.example.vacancies.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.example.vacancies.models.Vacancy

@Database(entities = [Vacancy::class], version = 1)
abstract class VacancyDatabase : RoomDatabase() {
    abstract fun vacancyDao() : VacancyDao
}