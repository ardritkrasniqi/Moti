package com.ardritkrasniqi.moti.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ardritkrasniqi.moti.R
import com.ardritkrasniqi.moti.UtilityClasses.Constants
import com.ardritkrasniqi.moti.UtilityClasses.PrefUtils
import com.ardritkrasniqi.moti.ui.todayFragment.TodayFragment
import com.google.android.gms.location.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MainActivity : AppCompatActivity(){
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var geocoder: Geocoder
    private val PERMISSION_ID = 1000
    private lateinit var sharedPref: PrefUtils



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        geocoder = Geocoder(this)
        sharedPref = PrefUtils(this, Constants.SHAREDPREFF_NAME, Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)

        if(sharedPref.getString(Constants.SELECTED_CITY, "null").equals("null")){
           GlobalScope.launch { getLastLocation() }
        }

    }



    suspend fun getLastLocation(): String {
        return suspendCoroutine { coroutine ->
            var adress: MutableList<Address>
            if (checkPermission()) {
                if (isLocationEnabled()) {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                        val location = task.result
                        if (location != null) {
                            adress =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            coroutine.resume(adress[0].getAddressLine(0))
                            var cityName: String = ""
                            val adressArrays = adress[0].getAddressLine(0).split(",")
                            cityName = adressArrays.last().replace("\\s".toRegex(), "")
                            sharedPref.save(Constants.SELECTED_CITY, cityName)
                        } else {
                            getNewLocation()
                        }
                    }
                } else {
                    runOnUiThread { turnLocationOn() }
                }
            } else {
                requestPermission()
            }
        }

    }

    fun turnLocationOn(){
        Toast.makeText(
            applicationContext,
            "Location is off!",
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 2
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {

        }
    }

    private fun checkPermission(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("LocationPermission", "You have permission")
                if(sharedPref.getString(Constants.SELECTED_CITY, "null").equals("null")){
                    GlobalScope.launch { getLastLocation() }
                }
            }
        }
    }
}

