package com.github.skrox.travelally.data.network.responses

import com.github.skrox.travelally.data.db.entities.Trip

data class TripsResponse(val trips: List<Trip>)