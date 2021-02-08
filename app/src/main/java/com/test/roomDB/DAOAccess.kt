package com.test.roomDB

import androidx.room.*
import com.test.models.MetricsDataModel
import com.test.models.MetricsModel


@Dao
interface DAOAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertData(metrtic: MetricsModel): Void

    /*

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        //@Transaction
        suspend fun InsertDataM(countryList: MetricsDataModel): Void
    */
    @TypeConverter
    @Query("SELECT * FROM metrics_details")
    fun loadmetricData(): MetricsModel?
}