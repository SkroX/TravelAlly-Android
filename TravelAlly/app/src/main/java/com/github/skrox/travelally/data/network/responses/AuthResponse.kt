package com.github.skrox.travelally.data.network.responses

import com.github.skrox.travelally.data.db.entities.User

data class AuthResponse (val token:String,
                         val user:User)