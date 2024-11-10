package dev.pegasus.mediaobserver

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
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
        initContentObserver()
    }

    override fun onPause() {
        super.onPause()
        mediaContentObserver.unregister()
    }

    private fun initContentObserver() {
        mediaContentObserver = MediaContentObserver(contentResolver) {
            // Changes have been detected
            changeObserved()
        }
    }

    private fun changeObserved() {
        counter++
        val text = "MediaStore has been updated ($counter) time/s"
        
        // Display change notification
        findViewById<MaterialTextView>(R.id.mtv_title).text = text
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

        // Fetch details of recent changes
        getRecentMediaChanges()
    }

    private fun getRecentMediaChanges() {
        val projection = arrayOf(
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED
        )

        val selection = "${MediaStore.Files.FileColumns.DATE_ADDED} >= ?"
        val oneMinuteAgo = (System.currentTimeMillis() / 1000) - 60
        val selectionArgs = arrayOf(oneMinuteAgo.toString())

        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        )

        cursor?.use {
            val nameIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val pathIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

            while (cursor.moveToNext()) {
                val fileName = cursor.getString(nameIndex)
                val filePath = cursor.getString(pathIndex)

                // Show file path and name in Toast or log it
                Toast.makeText(this, "File Added: $fileName\nPath: $filePath", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
