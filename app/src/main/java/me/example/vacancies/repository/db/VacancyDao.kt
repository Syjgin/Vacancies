package me.example.vacancies.repository.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.example.vacancies.models.Vacancy

@Dao
interface VacancyDao {
    @Insert(onConflict = REPLACE)
    fun save(entity: List<Vacancy>)
    @Query("SELECT * FROM vacancy ORDER BY page DESC")
    fun getByPage() : DataSource.Factory<Int, Vacancy>
    @Query("SELECT * from vacancy WHERE title LIKE :request ORDER BY page DESC")
    fun getByQuery(request: String) : DataSource.Factory<Int, Vacancy>
}