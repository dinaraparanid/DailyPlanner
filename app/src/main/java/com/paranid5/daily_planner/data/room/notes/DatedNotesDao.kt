package com.paranid5.daily_planner.data.room.notes

import androidx.room.Dao
import androidx.room.Query
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface DatedNotesDao : BaseDao<DatedNote.Entity> {
    @Query("SELECT * FROM DatedNote")
    fun getAllWithFlow(): Flow<List<DatedNote.Entity>>

    @Query("SELECT * FROM DatedNote")
    suspend fun getAll(): List<DatedNote.Entity>

    @Query("SELECT * FROM DatedNote WHERE id = (:id)")
    fun getByIdWithFlow(id: Long): Flow<DatedNote.Entity?>

    @Query("SELECT * FROM DatedNote WHERE id = (:id)")
    suspend fun getById(id: Long): DatedNote.Entity?
}