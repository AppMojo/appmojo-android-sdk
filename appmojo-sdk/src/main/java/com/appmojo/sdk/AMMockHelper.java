package com.appmojo.sdk;

import com.appmojo.sdk.mock.AMMock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nutron on 7/3/15 AD.
 */
public class AMMockHelper {

    public Map<String, AMConfiguration> generateConfiguration(Map<String, AMMock> mockDatas) {
        if(mockDatas != null) {
            return createConfigurationObject(mockDatas);
        } else {
            return new HashMap<>();
        }
    }

    private Map<String, AMConfiguration> createConfigurationObject(Map<String, AMMock> mockDatas) {

        Map<String, AMConfiguration> configMap = new HashMap<>();

        AMMock amMock;
        AMBannerConfiguration bannerConfig = null;
        AMInterstitialConfiguration interstitialConfig = null;
        Set<String> keys =  mockDatas.keySet();
        for(String key : keys) {
            amMock = mockDatas.get(key);
            if(amMock.getAdType() == AMAdType.BANNER) {
                bannerConfig = new AMBannerConfiguration();
                bannerConfig.setAdUnitId(amMock.getAdUitId());
                bannerConfig.setAdNetwork(amMock.getAdNetwork());
                bannerConfig.setRefreshRate(amMock.getRefresh());
                configMap.put(key, bannerConfig);
            } else {
                interstitialConfig = new AMInterstitialConfiguration();
                interstitialConfig.setAdUnitId(amMock.getAdUitId());
                interstitialConfig.setAdNetwork(amMock.getAdNetwork());
                interstitialConfig.setSessionFrequency(amMock.getSessionFrequency());
                interstitialConfig.setHourFrequency(amMock.getHourFrequency());
                interstitialConfig.setDayFrequency(amMock.getDayFrequency());
                configMap.put(key, interstitialConfig);
            }
        }
        return configMap;
    }
}
