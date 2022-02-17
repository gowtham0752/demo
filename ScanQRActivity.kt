package com.fiserv.dps.mobile.sdk.Activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.fiserv.dps.mobile.sdk.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.activity_scan_qr.*
import java.util.*

class ScanQRActivity : AppCompatActivity() {
    private var isFlashOn = false

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_scan_qr)
        isFlashOn = false
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(BarcodeFormat.CODE_39) // Set barcode type
        barcode_scanner!!.getBarcodeView().decoderFactory = DefaultDecoderFactory(formats)
        barcode_scanner!!.initializeFromIntent(intent)
        barcode_scanner!!.setStatusText("")
        barcode_scanner!!.decodeContinuous(callback)
        if (!hasFlash()) {
            btn_flash.visibility = View.GONE
        }
        btn_flash.setOnClickListener { switchFlashlight() }
        btn_cancel.setOnClickListener { finish() }
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            Log.e(TAG, "---------------------->" + result.text) // QR/Barcode result
            finish()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun switchFlashlight() {
        if (isFlashOn) {
            isFlashOn = false
            barcode_scanner!!.setTorchOff()
        } else {
            isFlashOn = true
            barcode_scanner!!.setTorchOn()
        }
    }

    override fun onResume() {
        super.onResume()
        barcode_scanner!!.resume()
    }

    override fun onPause() {
        super.onPause()
        barcode_scanner!!.pause()
    }

    companion object {
        private const val TAG = "ScanQRActivity"
    }
}