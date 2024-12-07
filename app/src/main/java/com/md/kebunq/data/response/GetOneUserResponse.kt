package com.md.kebunq.data.response

import com.google.gson.annotations.SerializedName

data class GetOneUserResponse(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("profile_image_id")
	val profileImageId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
