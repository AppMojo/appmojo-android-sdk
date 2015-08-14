package com.sample.appmojo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.appmojo.sdk.AMAdRequest;
import com.appmojo.sdk.AMBannerView;
import com.appmojo.sdk.base.AMAdSize;


public class AMBannerActivity extends Activity {
    private AMBannerView mXmlAdView;
    private AMBannerView mBannerView;
    private RelativeLayout mAdsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sample.appmojo.R.layout.activity_ambanner_layout);

        //setup ad request
        AMAdRequest.Builder builder = new AMAdRequest.Builder();
        builder.addTestDevice(getString(com.sample.appmojo.R.string.device_id));
        AMAdRequest adRequest = builder.build();

        //read view from xml
        mXmlAdView = (AMBannerView)findViewById(com.sample.appmojo.R.id.adView);
        mXmlAdView.loadAd(adRequest);

        //create banner in code
        mAdsLayout = (RelativeLayout) findViewById(com.sample.appmojo.R.id.ads_layout);
        mBannerView = new AMBannerView(getApplicationContext());
        mBannerView.setAdSize(AMAdSize.BANNER);
        mBannerView.setPlacementUid(getString(com.sample.appmojo.R.string.ad_placement_id_two));
        mBannerView.loadAd(adRequest);

        //add banner view to parent
        mAdsLayout.addView(mBannerView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**You MUST destroy object**/
        mXmlAdView.destroy();
        mBannerView.destroy();
    }
}
