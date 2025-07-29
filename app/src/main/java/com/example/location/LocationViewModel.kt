package com.example.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {
    private val _location= mutableStateOf<LocationData?>(null)
    val location: State<LocationData?> =_location
    fun addLocation(newLocation: LocationData){
        _location.value=newLocation
    }
}