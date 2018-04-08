package com.shuja1497.mapsinkotlin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SyncRequest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private lateinit var mMap: GoogleMap
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var lastLocation: Location

    private  var currentLocationMarker: Marker? = null

    public val REQUEST_LOCATION_CODE: Int = 99


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission()
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//        val a = this.checkPermission(Context.LOCATION_SERVICE,0,1)
//        Toast.makeText(this, a.toString(), Toast.LENGTH_LONG).show()
//        mMap.isMyLocationEnabled = true
//        mMap.uiSettings.isZoomControlsEnabled = true
//        mMap.uiSettings.isZoomGesturesEnabled = true
//        mMap.uiSettings.isCompassEnabled = true
//        mMap.uiSettings.isRotateGesturesEnabled = true
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient()
            mMap.isMyLocationEnabled = true

        }
    }

    public fun onClick(view:View){

        when(view.id){

            R.id.button_search->{

                val searchString = editText_search.text.toString()

                if (searchString.equals("")){
                    Toast.makeText(this, "Please enter a location", Toast.LENGTH_LONG).show()
                }
                else{

                    val geocoder = Geocoder(this)
                    val addresses: List<Address> = geocoder.getFromLocationName(searchString,30)
                    var markerOptions: MarkerOptions
                    var latLng:LatLng

                    addresses.forEach {
                        latLng = LatLng(it.latitude,  it.longitude)
                        markerOptions = MarkerOptions().position(latLng).title("Search Results")
                        mMap.addMarker(markerOptions)
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap.uiSettings.isZoomControlsEnabled=true
                    }
                    Toast.makeText(this, "Searched successfully", Toast.LENGTH_LONG).show()
                }
            }

            R.id.button_search_nearby->{
            }
        }
    }

    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        googleApiClient.connect()

    }


 // getting current location of the user
    @SuppressLint("RestrictedApi")
    override fun onConnected(p0: Bundle?) {

        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
        }
    }

    // setting the marker for the last location of the user
    override fun onLocationChanged(location: Location?) {
        lastLocation= location!!

        if (currentLocationMarker != null){
            currentLocationMarker?.remove()
        }

        val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)

        val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))

        currentLocationMarker = mMap.addMarker(markerOptions)

        // moving camera to a location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10.0F))

        // stop location update after setting current location
        if (googleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
    }

    // to check if the permission is granted or not
     fun checkLocationPermission(): Boolean{
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            // if permission not granted by the device then the app has to explicitly ask the user for the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)){
                // request the permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_CODE)
            } else
                run {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_LOCATION_CODE)
                }
            return false
        } else {
            return true
        }
    }

    // to check if the permission was granted or not .
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            REQUEST_LOCATION_CODE->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    // permission granted
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                                            == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient==null){
                            buildGoogleApiClient()
                        }
                        mMap.isMyLocationEnabled=true
                    }
                }else{
                    // permission denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

}
