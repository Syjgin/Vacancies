package me.example.vacancies.repository.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(context: Context) {
    private var database: VacancyDatabase = Room.databaseBuilder(context, VacancyDatabase::class.java, "main.db").build()

    @Provides
    @Singleton
    fun provideDatabase() : VacancyDatabase {
        return database
    }
}