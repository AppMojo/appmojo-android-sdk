package com.appmojo.sdk;

import com.appmojo.sdk.base.AMAdNetwork;


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
