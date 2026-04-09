package com.example.medication.core.di

import android.content.Context
import androidx.room.Room
import com.example.medication.core.database.AppDatabase
import com.example.medication.core.database.dao.FavoriteDao
import com.example.medication.core.database.dao.MedicationDao
import com.example.medication.core.database.dao.SearchMedicineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "medication_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMedicationDao(db: AppDatabase): MedicationDao =
        db.medicationDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao =
        db.favoriteDao()

    @Provides
    @Singleton
    fun provideSearchMedicineDao(db: AppDatabase): SearchMedicineDao =
        db.searchMedicineDao()
}