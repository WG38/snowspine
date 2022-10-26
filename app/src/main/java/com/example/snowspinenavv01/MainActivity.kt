package com.example.snowspinenavv01

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlin.math.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity()  {

    // variables for location accessing
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var locArray : Array<Double> // array that stores the location (lat,lon) queried from DisplayLocation

    // variables for heading accessing
    var headingHolder by Delegates.notNull<Float>() // stores the heading queried from onSensorChanged
    var bearingHolder by Delegates.notNull<Float>() // stores the bearing queried from calculateHeadingDirection
    private lateinit var mSensorManager: SensorManager
    private lateinit var mCompass: Sensor

    // variable for destination storage
    private lateinit var destinationArray : Array<Double>

    // variables for camera operation
    private var CAMERA_REQUEST = 100

    // variables for buttons
    private lateinit var enterLocMenu : Button
    private lateinit var enterCamera : Button




    //main function
    override fun onCreate(savedInstanceState: Bundle?) {
        //Creates the graphical interface
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this!!)
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        //initialize current location,heading and destination variables
        locArray = arrayOf(0.0,0.0)
        destinationArray = arrayOf(49.690888, 20.645186)
        headingHolder = 0f
        bearingHolder = 0.1f

        //get initial location
        isLocationPermissionGranted()
        getLastKnownLocation()

        //open location entry field
        enterLocMenu = findViewById(R.id.button_enter_loc)
        enterLocMenu.setOnClickListener() {
            openLocActivity()

        }

        isCameraPremissionGranted()
        //open camera (to be expanded later)
        enterCamera = findViewById(R.id.open_cam)
        enterCamera.setOnClickListener() {
            val intent = Intent(this,CameraActivity::class.java)
            startActivity(intent)
        }

        //get values from search menu
        val intent = intent
        val temp_lat = intent.getStringExtra("lat_value")
        val temp_lon = intent.getStringExtra("lon_value")
        if (temp_lon != null && temp_lat != null) {
            destinationArray = arrayOf(temp_lat.toDouble(),temp_lon.toDouble())
        }



        //destinationArray = arrayOf(TargetSearchMenu().lat_str.toDouble(),TargetSearchMenu().lon_str.toDouble())
        //getTargetLatLong()
       // startLocationUpdates()



        //getLastKnownLocation
        //var current_location: Double = 0.0

        //Log.d("FUCK2", current_location.toString())
        //var current_location_str = current_location.toString()
        //val current_location_text = findViewById<View>(R.id.loc_text) as TextView
        //current_location_text.text = current_location_str
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)




    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        mSensorManager.unregisterListener(sensorEventListener)

    }


    override fun onResume() {
        super.onResume()
        getLastKnownLocation()
        startLocationUpdates()
        mSensorManager.registerListener(sensorEventListener, mCompass, SensorManager.SENSOR_DELAY_NORMAL)




    }

    private var sensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            //println("what")
        }

        override fun onSensorChanged(event: SensorEvent) {
            val azimuth = Math.round(event.values[0]).toFloat()
            DisplayHeading(azimuth)
            //Log.d("FUCK2", azimuth.toString())

        }


    }
    //get magnetic heading
    //fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // The following method is required by the SensorEventListener interface;
    // Hook this event to process updates;
    //fun onSensorChanged(event: SensorEvent) {

        // The other values provided are:
        //  float pitch = event.values[1];
        //  float roll = event.values[2];
        //mTextView.setText("Azimuth: " + java.lang.Float.toString(azimuth))
    //}

    private fun DisplayHeading(azi:Float)
    {
        var current_azi_str = azi.toString()
        val current_azi_text = findViewById<View>(R.id.mag_text) as TextView
        current_azi_text.text = "Heading:" + current_azi_str + "°"
        headingHolder = azi
        val needle_img = findViewById<View>(R.id.comp_img) as ImageView
        needle_img.rotation = -azi
        calculateHeadingDirection(locArray[0],locArray[1],headingHolder,destinationArray)

    }

    //permissions
    private fun isCameraPremissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.CAMERA
                ),1

            )
            false
        } else {
            true
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),1

            )
            false
        } else {
            true
        }
    }

    private fun getLastKnownLocation(){ //myCallback: (Double) -> Double
        var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this!!)
        //var lat:Double = 4.0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


        }
        locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.smallestDisplacement = 1f // 170 m = 0.1 mile
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
        //Log.d("FUCK", "bruh fr2 ")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                Log.d("FUCK", "bruh fr ")
                // get latest location
                Log.d("FUCK3", "bruh fr ")
                val location = locationResult.lastLocation
                val lat = location?.latitude
                val lon = location?.longitude

                if (lat != null) {
                    if (lon != null) {
                        Log.d("FUCK2", "bruh fr ")
                        DisplayLocation(lat,lon)

                    }
                }
                // use your location object
                // get latitude , longitude and other info from this

                //mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,object : CancellationToken() {
            //override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            //override fun isCancellationRequested() = false
        //}).addOnSuccessListener{ location: Location? ->

            //if (location == null) {
                //Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                //var current_location_text = findViewById<View>(R.id.loc_text) as TextView
               // current_location_text.text = location.toString()
            //}
            //else {

                //val lat = location.latitude
                //val lon = location.longitude
                //DisplayLocation(lat,lon)


                //var current_location_str = lat.toString() + lon.toString()
                //var current_location_text = findViewById<View>(R.id.loc_text) as TextView
                //current_location_text.text = current_location_str
                //myCallback(lat)
                //Log.d("FUCK", myCallback(lat).toString())



            }
        }
        //return m
    }
    fun DisplayLocation(latitude:Double,longitude:Double)
    {
        var current_location_str = latitude.toString() + " " + longitude.toString()
        val current_location_text = findViewById<View>(R.id.loc_text) as TextView
        current_location_text.text = "Current Location: $current_location_str"
        Log.d("FUCK5", latitude.toString())
        locArray = arrayOf<Double>(latitude,longitude)
        bearingHolder = calculateHeadingDirection(locArray[0],locArray[1],headingHolder,destinationArray)

    }
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)


    }


    //choose a point on map then determine the distance from current location to that point and also
    //what is its heading compared to north (where is it on the compass)

    @SuppressLint("SetTextI18n")
    public fun calculateHeadingDirection(lat1: Double, lon1: Double, heading:Float, pointLoc : Array<Double>) : Float {
        //distance
        val Radius = 6371e3// m
        val lat2 = pointLoc[0]
        val lon2 = pointLoc[1]
        val mathPi = Math.PI/180
        val phi1 = lat1 * mathPi
        val phi2 = lat2 * mathPi
        val lamb1 = lon1*mathPi
        val lamb2 = lon2*mathPi
        val delPhi = (lat2-lat1) * mathPi
        val delLamb = (lon2-lon1) * mathPi
        val a = sin(delPhi / 2).pow(2.0) +
                cos(phi1) * cos(phi2) * sin(delLamb / 2).pow(2.0)

        //val abs_a = abs(a)
        //val check_this = cos(phi1) * cos(phi2)
        //val and_this = pow(Math.sin(delPhi/2),2.0)
        val c = 2 * atan2(Math.sqrt(a), Math.sqrt(1-a))
        //val d2 = acos( sin(phi1) * sin(phi2) + cos(phi1) *cos(phi2) * cos(delLamb) ) * Radius /1000
        val d = Radius * c / 1000
        //Log.d("FUCK5", lat1.toString())

        //bearing
        val y = sin(lamb2 - lamb1 ) * cos(phi2)
        val x = cos(phi1)*sin(phi2) - sin(phi1)*cos(phi2)*cos(lamb2 - lamb1)
        val angle = atan2(y,x)
        val bearing =  (angle*180/Math.PI + 360) % 360 // to degrees

        val dist_bear_text = findViewById<View>(R.id.dist_bear_text) as TextView
        dist_bear_text.text = "Distance to Point: $d km \n Point Bearing: $bearing"

        val bear_img = findViewById<View>(R.id.target_pointer) as ImageView
        bear_img.rotation = - heading + bearing.toFloat()

        return bearing.toFloat()


        //var current_location_str = latitude.toString() + " " + longitude.toString()
        //val current_location_text = findViewById<View>(R.id.loc_text) as TextView
        //current_location_text.text = current_location_str
    }
    private fun openLocActivity()
    {
        val intent = Intent(this,TargetSearchMenu::class.java)
        startActivity(intent)
    }










}


