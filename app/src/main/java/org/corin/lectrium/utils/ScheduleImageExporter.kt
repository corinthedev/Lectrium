// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.Toast

object ScheduleImageExporter {
    fun exportViewAsBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun notifyExportSuccess(context: Context) {
        Toast.makeText(context, "Schedule exported as image successfully!", Toast.LENGTH_SHORT)
            .show()
    }
}
