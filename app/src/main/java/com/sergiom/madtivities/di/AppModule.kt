package com.sergiom.madtivities.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sergiom.madtivities.data.local.AppDatabase
import com.sergiom.madtivities.data.local.EventsDao
import com.sergiom.madtivities.data.remote.EventRemoteDataSource
import com.sergiom.madtivities.data.remote.EventService
import com.sergiom.madtivities.data.repository.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://datos.madrid.es/egob/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideEventsService(retrofit: Retrofit): EventService = retrofit.create(EventService::class.java)

    @Singleton
    @Provides
    fun provideEventsRemoteDataSource(eventService: EventService) = EventRemoteDataSource(eventService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideEventsDao(db: AppDatabase) = db.eventsDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: EventRemoteDataSource,
                          localDataSource: EventsDao) =
        EventsRepository(remoteDataSource, localDataSource)
}