package dev.pegasus.mediaobserver.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore

class MediaContentObserver(
    private val contentResolver: ContentResolver,
    private val onChangeCallback: (Uri?) -> Unit // Callback now called instantly
) : ContentObserver(null) { // Removed Handler, so Looper is null

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        
        // Invoke callback immediately on change detection
        onChangeCallback.invoke(uri)
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
