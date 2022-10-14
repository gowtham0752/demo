package com.temenos.zellenfi;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.graphics.Color;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

import com.fiserv.dps.mobile.sdk.bridge.controller.Bridge;
import com.fiserv.dps.mobile.sdk.bridge.model.Zelle;
import com.fiserv.dps.mobile.sdk.bridge.view.BridgeView;
import com.konylabs.android.KonyMain;

public class ZelleBridgeViewWrapper {
    LinearLayout linearLayout;
    private static final int CONTENT_VIEW_ID = 10101010;
    @SuppressLint("SetJavaScriptEnabled")
    public LinearLayout loabZelleBridgeView(Context applicationContext){

        Context konyAppContext = KonyMain.getAppContext();

        KonyMain konyactContext = KonyMain.getActContext();
        KonyMain konyactivityContext = KonyMain.getActivityContext();

//
        Log.d("--------konyAppContext is:", ""+konyAppContext);
        Log.d("---------konyactContext is:", ""+konyAppContext);
        Log.d("--------konyactivityContext is:", ""+konyAppContext);

        //Activity mActivity = (Activity)konyAppContext;
        this.linearLayout = new LinearLayout(applicationContext);
        this.linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rightGravityParams = new LinearLayout.LayoutParams(-1, -1);
//        this.webView = new WebView(konyContext);
        //************************************
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        params.put("key3", "value3");

        Map<String, String> pdContact= new HashMap<String, String> ();
        pdContact.put("title", "We would like to access your phone contacts"); //prominent disclosure title
        pdContact.put("message", "We only sync phone numbers and email addresses from your contact list to help you add and pay a new recipient in Zelle®"); //prominent disclosure message

        Map<String, String> pdCamera= new HashMap<String, String> ();
        pdCamera.put("title", "We would like to access your camera"); //prominent disclosure title
        pdCamera.put("message", "We only access your camera to help you add and pay a new recipient in Zelle®"); //prominent disclosure message

        Map<String, String> pdPhotos= new HashMap<String, String> ();
        pdPhotos.put("title", "We would like to access your photos"); //prominent disclosure title
        pdPhotos.put("message", "We only access your photos to help you add and pay a new recipient in Zelle®"); //prominent disclosure message

        Map<String, Map<String, String>> appData = new HashMap<String, Map<String, String>>();
        appData.put("pd_contact", pdContact);
        appData.put("pd_camera", pdCamera);
        appData.put("pd_gallery", pdPhotos);


        Zelle zelle = new Zelle(
                "Demo Bank", //applicationName (Optional)
                "https://certtransfers.fta.cashedge.com/popnet/faces/loginServlet", //baseURL
                "88850171", //institutionId
                "zelle", //product
                "de21eef8c23702ab272b5b993ddcee75", // ssoKey
                null,
                null
        );

        //Activity mActivity = (Activity)konyAppContext;
        Bridge bridge = new Bridge(applicationContext, zelle);
        // optionally: set the contact pre-caching flag (default: false)
        zelle.setPreCacheContacts(true);
        BridgeView bridgeView =  bridge.view();
        Log.d("bridgeView is:","" + bridgeView);
        //getSupportFragmentManager().beginTransaction().replace(R.id.textView,
        //zelleView).commit();
        Log.d("bridgeView.getView() is:","" + bridgeView.getView());


        FragmentManager fragmentManager = ((FragmentActivity) applicationContext ).getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.add(CONTENT_VIEW_ID, bridgeView).commit();

        FrameLayout FL = new FrameLayout(applicationContext);
        FL.setBackgroundColor(Color.parseColor("#00ff00"));
        FL.setLayoutParams(rightGravityParams);
        FL.setId(CONTENT_VIEW_ID);
        this.linearLayout.addView(FL, rightGravityParams);
        Log.d("-----------this.linearLayout is:","" + this.linearLayout);
        return this.linearLayout;
    }
}
