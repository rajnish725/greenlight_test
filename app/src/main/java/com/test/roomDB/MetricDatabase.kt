package com.test.roomDB

import android.content.Context
import androidx.room.*
import com.test.models.MetricsModel
import com.test.models.ListTypeConverter

@Database(
    entities = [(MetricsModel::class)],
    version = 1,
    exportSchema = false
)
@TypeConverters(ListTypeConverter::class)
abstract class MetricDatabase : RoomDatabase() {

    abstract fun accessDao(): DAOAccess

    companion object {

        @Volatile
        private var INSTANCE: MetricDatabase? = null

        fun getDataseClient(context: Context): MetricDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, MetricDatabase::class.java, "TestAppDB")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }

}