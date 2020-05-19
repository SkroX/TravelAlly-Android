package com.github.skrox.travelally.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID=0

@Entity
data class User(val id:Int, val email:String, val name:String?, val picture:String?,
                val given_name: String?, val family_name:String?, val is_staff:Boolean,
                val is_active:Boolean){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}