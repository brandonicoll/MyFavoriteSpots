package com.example.myfavoritespots.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfavoritespots.R
import com.example.myfavoritespots.adapters.HappyPlacesAdapter
import com.example.myfavoritespots.database.DatabaseHandler
import com.example.myfavoritespots.models.HappyPlaceModel
import com.example.myfavoritespots.utils.SwipeToDeleteCallback
import com.example.myfavoritespots.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddHappyPlace.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }

        getHappyPlacesListFromLocalDatabase()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        if (id == R.id.action_one) {
            Toast.makeText(this, "Item One Clicked", Toast.LENGTH_LONG).show()
            return true

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getHappyPlacesListFromLocalDatabase()
    }

    private fun getHappyPlacesListFromLocalDatabase() {
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList = dbHandler.getHappyPlacesList() //gets happy place list from DatabaseHandler.kt get class

        if (getHappyPlaceList.size > 0) { //for testing until i add a recycler view
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupHappyPlacesRecyclerView(getHappyPlaceList)
        }
        else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>) {
        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        rv_happy_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object: HappyPlacesAdapter.OnClickListener{ //adapters cant have onclicklisteners so this must be done
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list) //attach the touch helper to the recycler view

        //for swipe to delete
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val customDialog = Dialog(this@MainActivity)

                customDialog.setContentView(R.layout.dialog_custom_back_confirmation) //the custom dialog we just made - set the xml as the view of it
                customDialog.tvYes.setOnClickListener {
                    val adapter = rv_happy_places_list.adapter as HappyPlacesAdapter
                    adapter.removeAt(viewHolder.adapterPosition)

                    getHappyPlacesListFromLocalDatabase()
                    customDialog.dismiss()
                }
                customDialog.tvNo.setOnClickListener {
                    getHappyPlacesListFromLocalDatabase()
                    customDialog.dismiss()
                }
                customDialog.show()

            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)
    }

    companion object {
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }

}