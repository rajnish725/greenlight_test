package com.test.models

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import java.sql.Array


/**
 * Created by Rajnish Yadav on 06 Feb 2021 at 18:16.
 */

@Entity(tableName = "metrics_details")
@TypeConverters(ListTypeConverter::class)
data class MetricsModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    // @ColumnInfo(name = "country")
    @SerializedName("country")
    // @TypeConverters(TypeConverters::class)
    var countryList: ArrayList<MetricsDataModel>? = arrayListOf(),

    @SerializedName("region")
    //@TypeConverters(TypeConverters::class)
    val regionList: ArrayList<MetricsDataModel>? = arrayListOf(),


    @SerializedName("zone")
    //@TypeConverters(TypeConverters::class)
    val zoneList: ArrayList<MetricsDataModel>? = arrayListOf(),

    @SerializedName("area")
    //@TypeConverters(TypeConverters::class)
    val areaList: ArrayList<MetricsDataModel>? = arrayListOf(),

    @SerializedName("employee")
    //@TypeConverters(TypeConverters::class)
    val employeeList: ArrayList<MetricsDataModel>? = arrayListOf()

)


class ListTypeConverter : Serializable {
    @TypeConverter
    fun stringToMetrics(json: String?): ArrayList<MetricsDataModel> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<MetricsDataModel?>?>() {}.type
        return gson.fromJson<ArrayList<MetricsDataModel>>(json, type)
    }

    @TypeConverter
    fun metricsToString(list: ArrayList<MetricsDataModel?>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<MetricsDataModel?>?>() {}.type
        return gson.toJson(list, type)
    }


    /*@TypeConverter
    fun areaToString(list: List<MetricsDataModel1?>?): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<MetricsDataModel1?>?>() {}.type
        return gson.toJson(list, type)
    }*/
}

//@Entity(tableName = "metrics_data")
data class MetricsDataModel(
    //  @PrimaryKey(autoGenerate = true)
    val id: Int,
    val country: String?,
    val territory: String?,
    val name: String?,
    val area: String?,
    val zone: String,
    val region: String
)


//@Entity(tableName = "metrics_data")
data class MetricsDataModel1(
    //  @PrimaryKey(autoGenerate = true)
    val id: Int,
    //@ColumnInfo(name = "country")
    val country: String?,


    val territory: String?,
    //@ColumnInfo(nameDes = "name")
    val name: String?,
    //@ColumnInfo(name = "area")
    val area: String?
)