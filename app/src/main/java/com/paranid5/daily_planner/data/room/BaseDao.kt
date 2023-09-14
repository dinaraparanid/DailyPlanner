package com.paranid5.daily_planner.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface BaseDao<T : Entity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entities: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg entities: T)

    @Delete
    suspend fun delete(entity: T)
}