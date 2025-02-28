package com.dicoding.mystoryapp.api.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem> = emptyList(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

@Parcelize
@Entity(tableName = "story")
data class ListStoryItem(

	@PrimaryKey
	@field:SerializedName("id")
	@ColumnInfo(name = "id")
	val id: String,

	@field:SerializedName("photoUrl")
	@ColumnInfo(name = "photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	@ColumnInfo(name = "createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	@ColumnInfo(name = "name")
	val name: String? = null,

	@field:SerializedName("description")
	@ColumnInfo(name = "description")
	val description: String? = null,

	@field:SerializedName("lon")
	@ColumnInfo(name = "lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	@ColumnInfo(name = "lat")
	val lat: Double? = null
) : Parcelable
