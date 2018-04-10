package com.shuja1497.mapsinkotlin

import java.net.SecureCacheResponse
import java.util.*

data class Location(val lat: Double, val lng:Double)

data class Geometry(val location: Location)

data class Result(val geometry: Geometry,
                          val vicinity: String)

data class Response(val html_attributions: List<Objects>,
                         val results: List<Result>,
                         val status: String)
