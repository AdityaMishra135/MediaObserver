package dev.pegasus.mediaobserver.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore

class MediaContentObserver(
    private val contentResolver: ContentResolver,
    private val onChangeCallback: (Uri?) -> Unit // Updated to include Uri parameter
) : ContentObserver(Handler(Looper.getMainLooper())) {

    private var lastTimeOfCall = 0L
    private var lastTimeOfUpdate = 0L
    private var thresholdTime: Long = 5000

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)

        lastTimeOfCall = System.currentTimeMillis()

        if (lastTimeOfCall - lastTimeOfUpdate > thresholdTime) {
            onChangeCallback.invoke(uri)  // Pass the Uri to the callback
            lastTimeOfUpdate = System.currentTimeMillis()
        }
    }

    fun register() {
        contentResolver.registerContentObserver(
            MediaStore.Files.getContentUri("external"), true, this
        )
    }

    fun unregister() {
        contentResolver.unregisterContentObserver(this)
    }
}
