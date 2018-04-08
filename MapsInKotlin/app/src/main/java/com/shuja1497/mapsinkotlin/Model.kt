package com.shuja1497.mapsinkotlin

data class Location(val lat: String, val lng:String)

data class Geometry(val location: Location)

data class Vicinity(val vicinity: Vicinity)

data class SingleResponse(val geometry: Geometry,
                          val vicinity: Vicinity)

data class Result(val results: List<SingleResponse>)
