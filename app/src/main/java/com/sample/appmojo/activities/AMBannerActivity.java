package com.sample.appmojo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.appmojo.sdk.AMAdRequest;
import com.appmojo.sdk.AMBannerView;
import com.appmojo.sdk.base.AMAdSize;
import com.sample.appmojo.R;


public class AMBannerActivity extends Activity {
    private AMBannerView mXmlAdView;
    private AMBannerView mBannerView;
    private RelativeLayout mAdsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sample.appmojo.R.layout.activity_ambanner_layout);

        //setup ad request
        String[] deviceIds = getResources().getStringArray(R.array.devices_uuid);
        AMAdRequest.Builder builder = new AMAdRequest.Builder();
        for(String uuid : deviceIds) {
            builder.addTestDevice(uuid);
        }
        AMAdRequest adRequest = builder.build();

        //read view from xml
        mXmlAdView = (AMBannerView)findViewById(com.sample.appmojo.R.id.adView);
        mXmlAdView.loadAd(adRequest);

        //create banner in code
        mAdsLayout = (RelativeLayout) findViewById(com.sample.appmojo.R.id.ads_layout);
        mBannerView = new AMBannerView(getApplicationContext());
        mBannerView.setAdSize(AMAdSize.BANNER);
        mBannerView.setPlacementUid(getString(com.sample.appmojo.R.string.banner_placement_id_two));
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
