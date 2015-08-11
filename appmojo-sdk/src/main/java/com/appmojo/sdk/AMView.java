package com.appmojo.sdk;

import com.appmojo.sdk.base.AMAdNetwork;

/**
 * Created by nutron on 6/25/15 AD.
 */
interface AMView {

    void loadAd(AMAdRequest adRequest);

    void reloadAd();

    void setPlacementUid(String placementUid);

    String getPlacementUid();

    AMAdNetwork getAdNetwork();

    AMListener getListener();

    String getCurrentAdUnitId();

    void destroy();


}
