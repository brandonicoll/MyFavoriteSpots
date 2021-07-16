package com.example.myfavoritespots.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfavoritespots.R
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import kotlinx.android.synthetic.main.activity_add_happy_place.toolbar_add_place
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setSupportActionBar(toolbar_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_map.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}