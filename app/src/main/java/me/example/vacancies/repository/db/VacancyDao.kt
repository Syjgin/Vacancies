package me.example.vacancies.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.example.vacancies.models.Vacancy

@Dao
interface VacancyDao {
    @Insert(onConflict = REPLACE)
    fun save(entity: List<Vacancy>)
    @Query("SELECT * FROM vacancy")
    fun getAll() : LiveData<List<Vacancy>>
    @Query("SELECT * from vacancy WHERE title LIKE :request")
    fun getByQuery(request: String) : LiveData<List<Vacancy>>
}