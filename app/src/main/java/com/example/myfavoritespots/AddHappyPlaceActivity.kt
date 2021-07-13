package com.example.myfavoritespots

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.Uri.parse
import com.parse.Parse
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Xml.parse
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintAttribute.parse
import androidx.core.content.res.FontResourcesParserCompat.parse
import androidx.core.net.MailTo.parse
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import okhttp3.HttpUrl.parse
import okhttp3.MediaType.parse
import okhttp3.internal.http.StatusLine.parse
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpCookie.parse
import java.net.URI
import java.text.SimpleDateFormat
import java.time.Duration.parse
import java.time.Instant.parse
import java.time.LocalDateTime.parse
import java.time.LocalTime.parse
import java.time.OffsetDateTime.parse
import java.time.OffsetTime.parse
import java.util.*
import java.util.Date.parse
import java.util.Locale.LanguageRange.parse
import java.util.jar.Manifest
import java.util.logging.Level.parse

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    //by implemented the onclicklistener extension it requires an onlick fun instead of just doing it in the oncreate
    @SuppressLint("RestrictedApi")

    private val cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener //dont have to initialize it now

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_happy_place)

        setSupportActionBar(toolbar_add_place)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_add_place.setNavigationOnClickListener {
            onBackPressed()
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        et_date.setOnClickListener(this) //the click is handled in the onclick fun
        tv_add_image.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_date -> {
                DatePickerDialog(this@AddHappyPlaceActivity, dateSetListener,
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from Gallery", "Capture photo from Camera")
                pictureDialog.setItems(pictureDialogItems) { dialog, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            if(data!=null){
                val contentUri=data.data
                try{
                    val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
                    val saveImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                    Log.e("Saved Image:", "Path :: $saveImageToInternalStorage")
                    iv_place_image.setImageBitmap(selectedImageBitmap)
                    //iv_place_image.selectedImageImageView.setImageURI(contentUri)
                }
                catch (e:IOException){
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to load image from gallery", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    var resultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            val thumbNail : Bitmap = data!!.extras?.get("data") as Bitmap
            val saveImageToInternalStorage = saveImageToInternalStorage(thumbNail)
            Log.e("Saved Image:", "Path :: $saveImageToInternalStorage")
            iv_place_image.setImageBitmap(thumbNail)
        }
    }

    private fun takePhotoFromCamera() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    resultLauncherCamera.launch(cameraIntent)
                }
            }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        resultLauncherGallery.launch(galleryIntent)
                    }
                }
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                    showRationalDialogForPermissions()
                }
        }).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("You have turned off permissions for this feature. It can be enabled under the application settings.")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

        et_date.setText(sdf.format(cal.time).toString())
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg") //gives the file a random id

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }

}