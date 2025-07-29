package com.example.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationUtils(private val context: Context){
  private val _fusedLocation = LocationServices.getFusedLocationProviderClient(context)
    @SuppressLint("MissingPermission")
    fun getLocation(viewModel: LocationViewModel){
        val locationRequest= LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000).build()
        val locationCallbacks= object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let{
                    val location= LocationData(it.latitude,it.longitude)
                    viewModel.addLocation(location)
                }
            }
        }
        _fusedLocation.requestLocationUpdates(
            locationRequest,
            locationCallbacks,
            Looper.getMainLooper()
        )
    }
    fun showLocationPermission():Boolean{
        return ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    fun getAddress(locationData: LocationData): String{
        val geoCoder= Geocoder(context, Locale.getDefault())
        val coordinate=LatLng(locationData.latitude,locationData.longitude)
        val addresses: MutableList<Address>? = geoCoder.getFromLocation(
            coordinate.latitude,coordinate.longitude,1)
        return if(addresses?.isNotEmpty()==true){
            addresses[0].getAddressLine(0)
        }else {
            "Address Not Found"
        }
    }
}