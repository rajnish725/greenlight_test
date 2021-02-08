package com.test.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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

    /* @Query("SELECT * FROM LOGIN_DATABASE")
     fun getLoginDetails(username: String?) : LiveData<MetricsModel>
 */
}