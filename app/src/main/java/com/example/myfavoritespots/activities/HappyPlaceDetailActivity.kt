package com.example.myfavoritespots.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfavoritespots.R
import com.example.myfavoritespots.models.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import kotlinx.android.synthetic.main.activity_happy_place_detail.*

class HappyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_place_detail)

        var happyPlaceDetailModel : HappyPlaceModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) { //if it has this EXTRA_PLACE_DETAILS name then...
            happyPlaceDetailModel = intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }

        if (happyPlaceDetailModel != null) {
            setSupportActionBar(toolbar_happy_place_details)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title //title of the recycler view element

            toolbar_happy_place_details.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }
}