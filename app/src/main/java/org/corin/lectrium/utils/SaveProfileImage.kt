// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun SaveProfileImage(
    context: Context,
    sourceUri: Uri
): String {
    val destinationFile = File(context.filesDir, "user_profile_image.jpg")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
        destinationFile.outputStream().use { output -> input.copyTo(output) }
    }

    return destinationFile.absolutePath
}