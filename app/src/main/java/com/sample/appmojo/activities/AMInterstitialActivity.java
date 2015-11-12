package com.sample.appmojo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appmojo.sdk.AMAdRequest;
import com.appmojo.sdk.AMInterstitial;
import com.appmojo.sdk.AMInterstitialListener;
import com.sample.appmojo.R;


public class AMInterstitialActivity extends Activity {
    private static final String TAG = "AMInterstitialActivity";

    private AMInterstitial mInterstitialAd;
    private InterstitialCallBack mListener;
    private boolean isCallLoadAd = true;
    private Button mReloadBtn;
    private Button mShowAdBtn;
    private Button mNextPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aminterstitial_layout);

        mReloadBtn = (Button) findViewById(R.id.interAd_refresh_btn);
        mShowAdBtn = (Button) findViewById(R.id.interAd_show_btn);
        mNextPageBtn = (Button) findViewById(R.id.interAd_next_btn);

        setupInterstitialAd();
        setupButtonSate(isCallLoadAd);
    }

    private void setupInterstitialAd() {
        String interPlcId = getString(R.string.interstitial_placement_id);
        String[] deviceIds = getResources().getStringArray(R.array.devices_uuid);
        mListener = new InterstitialCallBack();

        mInterstitialAd = new AMInterstitial(getApplicationContext());
        mInterstitialAd.setPlacementUid(interPlcId);
        mInterstitialAd.setListener(mListener);

        AMAdRequest.Builder builder = new AMAdRequest.Builder();
        for(String uuid : deviceIds) {
            builder.addTestDevice(uuid);
        }
        mInterstitialAd.loadAd(builder.build());
    }


    private void setupButtonSate(boolean isLoadAd) {
        mReloadBtn.setEnabled(!isLoadAd);
        mShowAdBtn.setEnabled(isLoadAd);
        mNextPageBtn.setEnabled(isLoadAd);
    }

    public void onClickBtn(View v) {
        switch (v.getId()) {
            case R.id.interAd_refresh_btn:
                isCallLoadAd = true;
                mInterstitialAd.reloadAd();
                break;

            case R.id.interAd_show_btn:
                isCallLoadAd = false;
                onClickShowInterstitialAd(mInterstitialAd);
                break;

            case R.id.interAd_next_btn:
                isCallLoadAd = false;
                onClickGotoNextPage(mInterstitialAd);
                break;

            default:
                break;
        }
        setupButtonSate(isCallLoadAd);
    }

    @SuppressWarnings("ConstantConditions")
    private void onClickShowInterstitialAd(AMInterstitial interstitial) {
        if (interstitial.isLoaded()) {
            interstitial.show();
            Toast.makeText(getApplicationContext(), interstitial.getCurrentAdUnitId(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),  "InterstitialAd not ready to show!", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void onClickGotoNextPage(AMInterstitial interstitial) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.setListener(mListener);
            mInterstitialAd.show();
            Toast.makeText(getApplicationContext(), interstitial.getCurrentAdUnitId(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "InterstitialAd not ready to show!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), AMBannerActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInterstitialAd.destroy();
    }


    private class InterstitialCallBack extends AMInterstitialListener {
        @Override
        public void onAdLoaded(AMInterstitial view) {
            Log.d(TAG, "onAdLoaded...");
        }

        @Override
        public void onAdClosed(AMInterstitial view) {
            Log.d(TAG, "onAdClosed...");
            Intent intent = new Intent(getApplicationContext(), AMBannerActivity.class);
            startActivity(intent);
            //free listener
            mInterstitialAd.setListener(null);
        }
    }
}
