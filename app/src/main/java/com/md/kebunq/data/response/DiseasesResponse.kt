package com.md.kebunq.data.response

import com.google.gson.annotations.SerializedName

data class DiseasesResponse(

	@field:SerializedName("diseases")
	val diseases: List<DiseasesItem>
)

data class DiseasesItem(

	@field:SerializedName("treatment")
	val treatment: String,

	@field:SerializedName("disease_id")
	val diseaseId: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("temporary_image_url")
	val temporaryImageUrl: String,

	@field:SerializedName("disease_index")
	val diseaseIndex: Int,

	@field:SerializedName("analysis")
	val analysis: String,

	@field:SerializedName("plant_index")
	val plantIndex: Int,

	@field:SerializedName("article")
	val article: String
)
