package com.fiserv.dps.mobile.sdk.utils

import android.content.Context
import android.os.Build
import android.util.Log
import org.json.JSONObject

object EventTracker {

    fun sendEventResult(context: Context?, startTime: String, eventName:String, eventResults:Boolean, permission:Boolean, message:String) {
        try {
            val msgObject = JSONObject()
            msgObject.put("CorrelationId" , TimePickerUtil.getRandomString(25))
            msgObject.put("TimeStamp" , TimePickerUtil.getCurrentTime())
            msgObject.put("Duration" , TimePickerUtil.timeDifferenceInHour(TimePickerUtil.getCurrentTime(), startTime)[2])
            msgObject.put("Event" , eventName)
            msgObject.put("EventResults" , if (eventResults) "Success" else "Failed")

            val deviceInfoObject = JSONObject()
            deviceInfoObject.put("Resolution" , "${Constants.layoutWidth},${Constants.layoutHeight}")
            deviceInfoObject.put("Network" , DeviceInfoUtil.getCarrier(context).name)
            deviceInfoObject.put("OSVersion" , Build.VERSION.RELEASE)
            deviceInfoObject.put("Model" , Build.PRODUCT)
            deviceInfoObject.put("Device" , Build.MANUFACTURER)

            val eventResultObject = JSONObject()
            eventResultObject.put("StatusCode" , if (permission)"Allowed" else "Denied")
            eventResultObject.put("MessageCode" , message)

            val permissionObject = JSONObject()
            permissionObject.put("Contact" , PermissionUtil.checkPermissionForReadContact(context!!))
            permissionObject.put("Camera" , PermissionUtil.checkPermissionForCamera(context))
            permissionObject.put("Gallery" , PermissionUtil.checkPermissionForGallery(context))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionObject.put("Gallery" , PermissionUtil.checkPermissionForMedia(context))
            }

            val resultObject = JSONObject()
            resultObject.put("Msg" ,msgObject)
            resultObject.put("DeviceInfo" ,deviceInfoObject)
            resultObject.put("Permission" ,permissionObject)
            resultObject.put("EventResults" ,eventResultObject)

            Log.d("Event Tracker Result","-------------------${resultObject}")
        }catch (e:Exception){
            Log.d("Event Tracker Result","-------------------${e.message}")
        }
    }

    fun getPrintAndShareEvent(event:Boolean) {

        val jsonObject1 = JSONObject()

        jsonObject1.put("CorrelationId" , TimePickerUtil.getRandomString(25))

        jsonObject1.put("TimeStamp" , TimePickerUtil.getCurrentTime())

        jsonObject1.put("Duration" , TimePickerUtil.timeDifferenceInHour(

            TimePickerUtil.getCurrentTime(),

            TimePickerUtil.getCurrentTime())[2])

        jsonObject1.put("EventResults" ,"Success")

        if (event){
            jsonObject1.put("Event" , "Share QR Code")
        }else
            jsonObject1.put("Event" , "Print QR Code")


        val jsonObject2 = JSONObject()

        jsonObject2.put("Resolution" , "${Constants.layoutWidth},${Constants.layoutHeight}")

        jsonObject2.put("Network" , "")

        jsonObject2.put("OSVersion" , Build.VERSION.RELEASE)

        jsonObject2.put("Model" , Build.PRODUCT)

        jsonObject2.put("Device" , Build.MANUFACTURER)


        val jsonObject4 = JSONObject()

        jsonObject4.put("StatusCode" , "Success")

        if (event){
            jsonObject4.put("MessageCode" , "User shared the QR code")
        }else
            jsonObject4.put("MessageCode" , "User start to print the qr code")



        val jsonObject3 = JSONObject()

        jsonObject3.put("Msg" ,jsonObject1)

        jsonObject3.put("EventResults" ,jsonObject4)

        jsonObject3.put("DeviceInfo" ,jsonObject2)

        Log.d("Share Event Tracker","-------------------${jsonObject3}")
    }


    fun getLaunchWeb(context:Context?, url:String, startTime:String, eventResult:String) {
        val jsonObject1 = JSONObject()
        jsonObject1.put("URL" ,url)
        jsonObject1.put("CorrelationId" , TimePickerUtil.getRandomString(25))
        jsonObject1.put("TimeStamp" , startTime)
        jsonObject1.put("Duration" , TimePickerUtil.timeDifferenceInHour(TimePickerUtil.getCurrentTime(), startTime)[2])
        jsonObject1.put("EventResults" ,eventResult)
        jsonObject1.put("Event" , "LaunchWebUIFromSDK")

        val jsonObject2 = JSONObject()
        jsonObject2.put("Resolution" , "${Constants.layoutWidth},${Constants.layoutHeight}")
        jsonObject2.put("Network" , DeviceInfoUtil.getCarrier(context).name)
        jsonObject2.put("OSVersion" , Build.VERSION.RELEASE)
        jsonObject2.put("Model" , Build.PRODUCT)
        jsonObject2.put("Device" , Build.MANUFACTURER)


        val jsonObject3 = JSONObject()
        jsonObject3.put("Msg" ,jsonObject1)
        jsonObject3.put("DeviceInfo" ,jsonObject2)

        Log.d("Launch Event Tracker","-------------------${jsonObject3}")
    }

}