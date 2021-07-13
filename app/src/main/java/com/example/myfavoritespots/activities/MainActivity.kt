package com.example.myfavoritespots.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myfavoritespots.R
import com.example.myfavoritespots.database.DatabaseHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getHappyPlacesListFromLocalDatabase() {
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList = dbHandler.getHappyPlacesList() //gets happy place list from DatabaseHandler.kt get class

        if (getHappyPlaceList.size > 0) { //for testing until i add a recycler view
            for (i in getHappyPlaceList) {
                Log.e("Title", i.title)
            }
        }
    }

}