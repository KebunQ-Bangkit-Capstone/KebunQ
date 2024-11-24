package com.md.kebunq.data.response

import com.google.gson.annotations.SerializedName

data class ListPredictionResponse(

	@field:SerializedName("predictions")
	val predictions: List<PredictionsItem>
)

data class PredictionsItem(

	@field:SerializedName("treatment")
	val treatment: String,

	@field:SerializedName("prediction_id")
	val predictionId: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("confidence_score")
	val confidenceScore: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

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
