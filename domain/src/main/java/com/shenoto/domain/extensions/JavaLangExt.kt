/*
 * Copyright 2018 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils.fromJson
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shenoto.domain.R
import com.shenoto.domain.bases.BaseErrorModel
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.Result
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This file contains extension methods for the java.lang package.
 */

/**
 * Helper method to check if a [String] contains another in a case insensitive way.
 */
fun String?.containsCaseInsensitive(other: String?) =
        if (this == null && other == null) {
            true
        } else if (this != null && other != null) {
            toLowerCase().contains(other.toLowerCase())
        } else {
            false
        }

inline val RequestOptions.option: RequestOptions
    get() = this.placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

inline val Long.milliSecondsToTimer: String
    get() {
        return  String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(this) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this)),
                TimeUnit.MILLISECONDS.toSeconds(this) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this)))
    }

inline val Long.downloadSpeed: String
    get() {
        if (this < 0) {
            return ""
        }
        val kb = this.toDouble() / 1000.toDouble()
        val mb = kb / 1000.toDouble()
        val decimalFormat = DecimalFormat(".##")
        return when {
            mb >= 1 -> "${decimalFormat.format(mb)} MB/s"
            kb >= 1 -> "${decimalFormat.format(kb)} KB/s"
            else -> "$this B/s"
        }
    }

inline val Bitmap.darkenBitMap: Bitmap
    get() {
        val canvas = Canvas(this)
        val p = Paint(Color.RED)
        val filter = LightingColorFilter(-0x808081, 0x00000000)    // darken
        p.colorFilter = filter
        canvas.drawBitmap(this, Matrix(), p)
        return this
    }

inline val Drawable.darkenBitMap: Bitmap
    get() {
        val bitmap = this.toBitmap
        val canvas = Canvas(bitmap)
        val p = Paint(Color.RED)
        val filter = LightingColorFilter(-0x808081, 0x00000000)    // darken
        p.colorFilter = filter
        canvas.drawBitmap(bitmap, Matrix(), p)
        return bitmap
    }

inline val Drawable.blur: Bitmap
    get() {
        val bitmap = this.toBitmap
        return ImageUtils.fastBlur(bitmap,0.3F,0.5F)
    }


inline val Drawable.toBitmap: Bitmap
    get() {
        val bitmap: Bitmap = if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        if (this is BitmapDrawable) {
            if (this.bitmap != null) {
                return this.bitmap
            }
        }

        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        return bitmap
    }

/**
 * Helper extension to URL encode a [String]. Returns an empty string when called on null.
 */
inline val String?.urlEncoded: String
    get() = if (Charset.isSupported("UTF-8")) {
        URLEncoder.encode(this ?: "", "UTF-8")
    } else {
        // If UTF-8 is not supported, use the default charset.
        @Suppress("deprecation")
        URLEncoder.encode(this ?: "")
    }



fun String?.openBrowser() {
    val context: Context= ActivityUtils.getTopActivity()
    this?.let {
        if (it.startsWith("http")) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            customTabsIntent.launchUrl(context, Uri.parse(it))
        }
    }
}

fun Bitmap?.scale(context: Context?,fileName :String = "${Date().time}.jpg" , newWidth:Int,newHeight :Int ) :File {

    val newFile = File(context?.cacheDir, fileName)
    newFile.createNewFile()
    val bos = ByteArrayOutputStream()
    //Convert bitmap to byte array
    val image = ImageUtils.scale(this, newWidth, newHeight)
    image.compress(Bitmap.CompressFormat.JPEG, 50, bos)
    val bitmapData = bos.toByteArray()
    //write the bytes in file
    val fos = FileOutputStream(newFile)
    fos.write(bitmapData)
    fos.flush()
    fos.close()
    return newFile
}

fun Bitmap.createPaletteSync() :Palette? {
    return Palette.from(this).generate()
}

inline val Drawable?.createPaletteSync: Palette?
    get() {
        return this?.toBitmap?.createPaletteSync()
    }

fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

inline fun <reified T> HttpException.errorModel() : BaseErrorModel<T>?{
    return try {
        Gson().fromJson<BaseErrorModel<T>>(this.response().errorBody()!!.string())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        this.response().errorBody()?.close()
    }
}


//    fromJson<T>(json, object: TypeToken<T>() {}.type)

//        if (this == null && other == null) {
//            true
//        } else if (this != null && other != null) {
//            toLowerCase().contains(other.toLowerCase())
//        } else {
//            false
//        }