package com.github.skrox.travelally.data.network.responses

data class UserSuggestionResponse(
    val id:String,
    val picture: String?,
    val name: String, val family_name: String
)
