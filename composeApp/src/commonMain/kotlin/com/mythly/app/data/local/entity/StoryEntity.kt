package com.mythly.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.Value
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "stories")
@TypeConverters(StoryConverters::class)
data class StoryEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val moralLesson: String,
    val deities: List<Deity>,
    val epic: Epic,
    val values: List<Value>,
    val imageUrl: String,
    val readTimeMinutes: Int,
    val datePublished: Long,
    val audioUrl: String? = null,
    val isRead: Boolean = false,
    val isFavorite: Boolean = false,
    val lastReadAt: Long? = null
)

class StoryConverters {
    @TypeConverter
    fun fromValuesList(values: List<Value>): String {
        return Json.encodeToString(values)
    }

    @TypeConverter
    fun toValuesList(valuesString: String): List<Value> {
        return Json.decodeFromString(valuesString)
    }

    @TypeConverter
    fun fromDeitiesList(deities: List<Deity>): String {
        return Json.encodeToString(deities)
    }

    @TypeConverter
    fun toDeitiesList(deitiesString: String): List<Deity> {
        return Json.decodeFromString(deitiesString)
    }
}
