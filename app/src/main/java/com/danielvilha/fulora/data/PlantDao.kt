package com.danielvilha.fulora.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Insert
    suspend fun insert(plant: Plant)

    @Update
    suspend fun update(plant: Plant)

    @Query("SELECT * FROM plant ORDER BY name")
    fun getAll(): Flow<List<Plant>>

    @Query("SELECT * FROM plant WHERE id = :plantId")
    fun getById(plantId: Int): Flow<Plant>

    @Delete
    suspend fun delete(plant: Plant)
}
