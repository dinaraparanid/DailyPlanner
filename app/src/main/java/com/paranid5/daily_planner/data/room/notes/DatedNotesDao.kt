package com.paranid5.daily_planner.data.room.notes

import androidx.room.Dao
import androidx.room.Query
import com.paranid5.daily_planner.data.note.DatedNote
import com.paranid5.daily_planner.data.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface DatedNotesDao : BaseDao<DatedNote.Entity> {
    @Query("SELECT * FROM DatedNote")
    fun getAll(): Flow<List<DatedNote.Entity>>

    @Query("SELECT * FROM DatedNote WHERE id = (:id)")
    fun getById(id: Int): Flow<DatedNote.Entity?>
}