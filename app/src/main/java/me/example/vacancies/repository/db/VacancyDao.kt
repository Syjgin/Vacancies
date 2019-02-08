package me.example.vacancies.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.example.vacancies.models.Vacancy

@Dao
interface VacancyDao {
    @Insert(onConflict = REPLACE)
    fun save(entity: List<Vacancy>)
    @Query("SELECT * FROM vacancy WHERE page=:page")
    fun getByPage(page: Int) : List<Vacancy>
    @Query("SELECT * from vacancy WHERE title LIKE :request AND page=:page")
    fun getByQuery(request: String, page: Int) : List<Vacancy>
}