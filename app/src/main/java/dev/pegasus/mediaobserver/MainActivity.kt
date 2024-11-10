package dev.pegasus.mediaobserver

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView
import dev.pegasus.mediaobserver.observers.MediaContentObserver

class MainActivity : AppCompatActivity() {

    private lateinit var mediaContentObserver: MediaContentObserver
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initContentObserver()
    }

    override fun onResume() {
        super.onResume()
        mediaContentObserver.register()
    }

    override fun onPause() {
        super.onPause()
        mediaContentObserver.unregister()
    }

    private fun initContentObserver() {
        mediaContentObserver = MediaContentObserver(contentResolver) { uri ->
            // Changes have been detected
            changeObserved(uri)
        }
    }

    private fun changeObserved(uri: Uri?) {
        counter++
        val text = "MediaStore has been updated ($counter) time(s)"

        findViewById<MaterialTextView>(R.id.mtv_title).text = text

        // Fetch details of the changed media item
        uri?.let {
            getMediaDetails(it)
        }
    }

    private fun getMediaDetails(uri: Uri) {
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED
        )

        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
                val filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                val dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))

                // Set details to a TextView
                val detailsTextView = findViewById<MaterialTextView>(R.id.mtv_details)
                detailsTextView.text = "File Name: $fileName\nPath: $filePath\nDate Added: $dateAdded"
            }
        }
    }
}
