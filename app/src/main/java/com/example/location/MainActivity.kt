package com.example.location

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.location.ui.theme.LocationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocationTheme {
                val viewModel: LocationViewModel= viewModel()
                val context= LocalContext.current
                val locationUtils=LocationUtils(context)
                LocationDisplay(context,locationUtils,viewModel)
            }
        }
    }
}
@Composable
fun LocationDisplay(
    context: Context,
    locationUtils: LocationUtils,
    viewModel: LocationViewModel
){
    val launcherRequest= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permissions->
            if(permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true){
               locationUtils.getLocation(viewModel)
            }
            else{
                val rationaleRequest=ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if(rationaleRequest){
                    Toast.makeText(context,"We need your permission",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"Change it from manually",Toast.LENGTH_LONG).show()
                }
            }
        }
    )
    val location = viewModel.location.value

     val address=location?.let {
         locationUtils.getAddress(it)
     }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(location!=null){
                Text("Location: ${location.latitude} ${location.longitude} /n $address ", fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
            }
            else{
                Text("Can't Load the Location", fontWeight = FontWeight.Bold)
            }
            Button(onClick = {
                if(locationUtils.showLocationPermission()){
                    locationUtils.getLocation(viewModel)
                }else{
                    launcherRequest.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }){
                Text("Get Location")
            }
        }
}



