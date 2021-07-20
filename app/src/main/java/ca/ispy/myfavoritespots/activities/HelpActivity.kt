package ca.ispy.myfavoritespots.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ca.ispy.myfavoritespots.R
import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        setSupportActionBar(toolbar_help_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_help_activity.setNavigationOnClickListener {
            finish()
            onBackPressed()
        }
    }
}