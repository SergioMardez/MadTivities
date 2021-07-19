package com.sergiom.madtivities.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sergiom.madtivities.data.entities.MadEventItemDataBase

@Dao
interface EventsDao {

    @Query("SELECT * FROM events")
    fun getAllEvents() : LiveData<List<MadEventItemDataBase>>

    @Query("SELECT * FROM events WHERE uid = :uid")
    fun getEvent(uid: String): LiveData<MadEventItemDataBase>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<MadEventItemDataBase>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: MadEventItemDataBase)

}