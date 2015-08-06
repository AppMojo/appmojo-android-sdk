package com.appmojo.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.appmojo.sdk.base.AMAdNetwork;
import com.appmojo.sdk.utils.AMLog;

/**
 * Created by nutron on 6/23/15 AD.
 */
abstract class AMController {
    protected Context mContext;
    protected AMView mAMView;
    protected AMAdRequest mAdRequest;
    protected AMCustomAdRequest mCustomAdRequest;
    protected AMAppEngine mAppEngine;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AMLog.d("On received configuration change...");
            refreshAd();
        }
    };

    public AMController(Context context, AMView view) {
        this.mContext = context;
        this.mAMView = view;
        this.mAppEngine = AMAppEngine.getInstance();
        //add local broadcast receiver
        LocalBroadcastManager.getInstance(mContext.getApplicationContext())
                .registerReceiver(mMessageReceiver,
                        new IntentFilter(AMBaseConfiguration.ACTION_CONFIGURATION_CHANGE));
    }

    public AMView getAMView() {
        return mAMView;
    }

    public String getCurrentAdUnitId() {
        if(mCustomAdRequest != null) {
            return mCustomAdRequest.getAdUnitId();
        }
        return null;
    }

    public AMAdNetwork getAdNetwork() {
        if(mCustomAdRequest != null) {
            return mCustomAdRequest.getAdNetwork();
        }
        return AMAdNetwork.UNKNOWN;
    }

    public void onDestroy() {
        AMLog.d("onDestroy controller...");
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mMessageReceiver);
        mAppEngine = null;
        mContext = null;
        mAMView = null;
        destroy();
    }

    private void refreshAd(){
        AMLog.d("refreshAd...");
        try {
            loadAd(mAdRequest);
        } catch (Exception e) {
            AMLog.d("Cannot refresh ad, no configuration to be applied...", e);
        }
    }

    //Sub class implement
    public abstract void loadAd(AMAdRequest adRequest);
    public abstract void reloadAd();
    protected abstract void applyAdRequest(AMCustomAdRequest customAdRequest);
    protected abstract void destroy();

}
