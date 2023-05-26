package dev.pegasus.mediaobserver

import android.os.Bundle
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


    private fun initContentObserver() {
        mediaContentObserver = MediaContentObserver(contentResolver) {
            // Fetch Media Again (changes has been detected)
            changeObserved()
        }
        mediaContentObserver.register()
    }

    private fun changeObserved() {
        counter++
        val text = "MediaStore has been updated ($counter) time/s"

        findViewById<MaterialTextView>(R.id.mtv_title).text = text
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaContentObserver.unregister()
    }
}