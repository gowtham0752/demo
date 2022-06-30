package com.fiserv.dps.mobile.sdk.handlers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Base64
import android.webkit.JavascriptInterface
import androidx.core.content.FileProvider
import androidx.print.PrintHelper
import com.fiserv.dps.mobile.sdk.utils.Constants.Companion.APPLICATION_ID
import java.io.File
import java.io.FileOutputStream
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import android.os.StrictMode.VmPolicy
import androidx.annotation.RequiresApi
import java.io.OutputStream
import java.lang.reflect.Method
import android.os.StrictMode
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.fiserv.dps.mobile.sdk.bridge.controller.BridgeFragment
import com.fiserv.dps.mobile.sdk.utils.Constants
import com.fiserv.dps.mobile.sdk.utils.PermissionUtil
import org.json.JSONObject


/**
 * ShareHandler created to handle the share details passed from java script.
 * This interface will be called from java script.
 * base64Image, text and url will be passed from java script.
 * sharePhoto() function used to share photo, text and url (text and url is option fields).
 * printPhoto() function used to print the qr code using printer
 * shareText() function used to share text and url (url is option field).
 * Created by F5SP0MG on 21,June,2021
 */
interface ShareHandler {
    @JavascriptInterface
    fun sharePhoto(base64Image: String, text: String?, url: String?)

    @JavascriptInterface
    fun printPhoto(base64Image: String, text: String?, url: String?)

    @JavascriptInterface
    fun shareText(text: String, url: String?)
}

/**
 * ShareHandler class has been implemented here to perform their actions
 */
class ShareHandlerImpl(private val activity: BridgeFragment) : ShareHandler {

    var imageUri: Uri? = null

    //use application context to get contentResolver
    val contentResolver = activity.requireContext().contentResolver


    @JavascriptInterface
    override fun sharePhoto(base64Image: String, text: String?, url: String?) {
        shareImageOrText(base64Image, text, url)
    }

    override fun printPhoto(base64Image: String, text: String?, url: String?) {
        printImage(base64Image)
    }

    @JavascriptInterface
    override fun shareText(text: String, url: String?) {
        shareImageOrText(text = text, url = url)
    }

    /**
     * function used to print photo,
     * @param base64Image: String
     * Created by F5ZF5DH on 19,July,2021
     */
    private fun printImage(base64Image: String) {
        this.also {
            PrintHelper(activity.requireContext()).apply {
                scaleMode = PrintHelper.SCALE_MODE_FIT
            }.also { printHelper ->
                val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                printHelper.printBitmap("qr.png - test print", bitmap)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageInQ(bitmap: Bitmap):Uri {
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        fos?.flush()
        fos?.close()
        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.let {
            contentResolver.update(it, contentValues, null, null)
        }
        return imageUri!!
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    /**
     * shareImageORText function will be initiate intent with ACTION_SENT
     * @param base64Image
     * @param text
     * @param url
     * Created by F5ZF5DH on 15,July,2021
     */
    @SuppressLint("SetWorldReadable")
    private fun shareImageOrText(base64Image: String = "", text: String?, url: String?) {
        try {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.type = "*/*"
            if (base64Image.isNotEmpty()) {
                val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val file = File(
                    activity.requireContext().externalCacheDir,
                    File.separator.toString() + "Image.png"
                )
                val fOut = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
                file.setReadable(false, true)

//                val photoURI = FileProvider.getUriForFile(
//                    activity.applicationContext,
//                    "$APPLICATION_ID.provider",
//                    file
//                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    intent.putExtra(Intent.EXTRA_STREAM, saveImageInQ(bitmap))
                else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P){
                        val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                        m.invoke(null)
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                }
                else {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (!text.isNullOrEmpty()) {
                intent.putExtra(Intent.EXTRA_SUBJECT, text)
            }
            if (!url.isNullOrEmpty()) {
                intent.putExtra(Intent.EXTRA_TEXT, url)
            }
            shareQRResultLauncher.launch(Intent.createChooser(intent, "Share image via"))
               // activity.startActivity()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val shareQRResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                contentResolver.delete(imageUri!!,null, null)
            }catch (e: Exception){

            }
        }
}