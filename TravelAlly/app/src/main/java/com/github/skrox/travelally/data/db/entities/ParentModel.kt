package com.github.skrox.travelally.data.db.entities

data class ParentModel (
        val type: Int,
        val title : String = "",
        val children : List<Trip>
)