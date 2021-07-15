package com.example.myfavoritespots.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfavoritespots.R
import com.example.myfavoritespots.activities.AddHappyPlaceActivity
import com.example.myfavoritespots.activities.MainActivity
import com.example.myfavoritespots.database.DatabaseHandler
import com.example.myfavoritespots.models.HappyPlaceModel
import kotlinx.android.synthetic.main.item_happy_place.view.*

open class HappyPlacesAdapter(private val context: Context, private var list: ArrayList<HappyPlaceModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener : OnClickListener? = null //adapters cant have onclicklisteners so this must be done

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_happy_place, parent, false) //inflates the xml of the cardview
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description

            holder.itemView.setOnClickListener{ //setup onclick for each element of the recyclerview
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setOnClickListener(onClickListener: OnClickListener) { //adapters cant have onclicklisteners so this must be done
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel) //adapters cant have onclicklisteners so this must be done (override in main or wherever the recycler is)
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) { //for swipe
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun removeAt (position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDelete = dbHandler.deleteHappyPlace(list[position])

        if (isDelete > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}