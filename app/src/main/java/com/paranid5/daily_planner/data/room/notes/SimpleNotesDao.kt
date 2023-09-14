package com.paranid5.daily_planner.data.room.notes

import androidx.room.Dao
import androidx.room.Query
import com.paranid5.daily_planner.data.note.SimpleNote
import com.paranid5.daily_planner.data.room.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface SimpleNotesDao : BaseDao<SimpleNote> {
    @Query("SELECT * FROM SimpleNote")
    fun getAll(): Flow<List<SimpleNote>>

    @Query("SELECT * FROM SimpleNote WHERE id = (:id)")
    fun getById(id: Int): Flow<SimpleNote?>
}