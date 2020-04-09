package com.example.android.architecture.blueprints.todoapp.util

import android.R.attr.path
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList


object WallpaperUtils {

    val TAG = "WallpaperUtils"

    fun setWallpaper(context: Context?, view: View) {

        val wm = WallpaperManager.getInstance(context)
        val previousWallpaper = wm.drawable

        var newWallpaper: Bitmap? = null
        val job = GlobalScope.launch() {
            newWallpaper = BitmapUtils.getBitmapFromView(view)
        }
        job.invokeOnCompletion {
            saveImage(drawableToBitmap(previousWallpaper), context)
            wm.clear()
            wm.setBitmap(newWallpaper)
        }

//        val nw =  GlobalScope.async {
//            doSomethingUsefulOne(view)
//        }

//        nw.invokeOnCompletion {
//            saveImage(drawableToBitmap(previousWallpaper), context)
//            wm.setBitmap(nw.await())
//            wm.clear()
//        }
    }

    suspend fun doSomethingUsefulOne(view: View): Bitmap? {
        return BitmapUtils.getBitmapFromView(view)
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    fun clearWallpaper(context: Context?) {
        val wm = WallpaperManager.getInstance(context)
        wm.clear()
    }

    private fun getAppSpecificWallpaperStorageDir(context: Context?): File? {
        val file = File(context?.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), "wallpapers")
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created")
        }
        return file
    }

    private fun saveImage(imageToSave: Bitmap?, context: Context?) {
        val file = File(getAppSpecificWallpaperStorageDir(context), System.currentTimeMillis().toString())


        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            imageToSave?.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFilesList(context: Context?): List<File>? {
        val files: Array<File>? = getAppSpecificWallpaperStorageDir(context)?.listFiles()
        return files?.toList()
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() === Environment.MEDIA_MOUNTED
    }

    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() === Environment.MEDIA_MOUNTED ||
                Environment.getExternalStorageState() === Environment.MEDIA_MOUNTED_READ_ONLY
    }


}