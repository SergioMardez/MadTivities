package com.sergiom.madtivities.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sergiom.madtivities.data.entities.MadEventItemDataBase

@Dao
interface EventsDao {

    @Query("SELECT * FROM events")
    fun getAllEvents() : LiveData<List<MadEventItemDataBase>>

    @Query("SELECT * FROM events WHERE uid = :uid")
    fun getEvent(uid: String): LiveData<MadEventItemDataBase>

    @Query("SELECT * FROM events WHERE favourite = 1")
    fun getAllFavouriteEvents() : LiveData<List<MadEventItemDataBase>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(events: List<MadEventItemDataBase>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: MadEventItemDataBase)

    @Update
    suspend fun updateItem(event: MadEventItemDataBase)

}