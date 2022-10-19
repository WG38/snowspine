package com.example.snowspinenavv01

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.snowspinenavv01.databinding.ActivityTargetSearchMenuBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TargetSearchMenu : AppCompatActivity() {

    private lateinit var acceptInput : FloatingActionButton
    lateinit var latGetTarget : EditText
    lateinit var lonGetTarget : EditText
    private lateinit var destArrayTemp : Array<Double>
    var lat_str = ""
    var lon_str = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_search_menu)
        //Log.e("What the fuck","????")


        acceptInput = findViewById(R.id.accept_input_button)
        acceptInput.setOnClickListener() {
            latGetTarget = findViewById<View>(R.id.enter_lat_field) as EditText
            lonGetTarget = findViewById<View>(R.id.enter_lon_field) as EditText
            lat_str = latGetTarget.text.toString()
            lon_str = lonGetTarget.text.toString()
            //MainActivity().destinationArray = arrayOf(2.0,3.0)
            //destArrayTemp = arrayOf(lat_str.toDouble(),lon_str.toDouble())
            Log.d("FUCK9", "wtf")
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("lat_value",lat_str)
            intent.putExtra("lon_value",lon_str)
            startActivity(intent)
        //getTargetLatLong()
        }




    }



}