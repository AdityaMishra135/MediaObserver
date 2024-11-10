package dev.pegasus.mediaobserver.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.provider.MediaStore

class MediaContentObserver(
    private val contentResolver: ContentResolver,
    private val onChangeCallback: (Uri?) -> Unit
) : ContentObserver(null) {

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)

        // Invoke callback immediately on change detection
        onChangeCallback.invoke(uri)
    }

    fun register() {
        // Register observer specifically for images in MediaStore
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, this
        )
    }

    fun unregister() {
        contentResolver.unregisterContentObserver(this)
    }
}
