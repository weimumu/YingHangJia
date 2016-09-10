package com.yinghangjiaclient.weight;

import com.yinghangjiaclient.util.HttpUtil;
import com.yinghangjiaclient.weight.AdDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linzibo on 2016/9/3.
 */
public class ImageBanner {

    /**
     * 轮播广播模拟数据
     *
     * @return
     */
    public static List<AdDomain> getBannerAd() {
        List<AdDomain> adList = new ArrayList<AdDomain>();
        String url1 = HttpUtil.BASE_URL + "adv1.jpg";
        String url2 = HttpUtil.BASE_URL + "adv2.jpg";
        String url3 = HttpUtil.BASE_URL + "adv3.jpg";
        String url4 = HttpUtil.BASE_URL + "adv4.jpg";
        String url5 = HttpUtil.BASE_URL + "adv5.jpg";
        AdDomain adDomain = new AdDomain();
        adDomain.setId("108078");
        adDomain.setImgUrl(url1);
        adDomain.setAd(false);
        adList.add(adDomain);

        AdDomain adDomain2 = new AdDomain();
        adDomain2.setId("108078");
        adDomain2.setImgUrl(url2);
        adDomain2.setAd(false);
        adList.add(adDomain2);

        AdDomain adDomain3 = new AdDomain();
        adDomain3.setId("108078");
        adDomain3.setImgUrl(url3);
        adDomain3.setAd(false);
        adList.add(adDomain3);

        AdDomain adDomain4 = new AdDomain();
        adDomain4.setId("108078");
        adDomain4.setImgUrl(url4);
        adDomain4.setAd(false);
        adList.add(adDomain4);

        AdDomain adDomain5 = new AdDomain();
        adDomain5.setId("108078");
        adDomain5.setImgUrl(url5);
        adDomain5.setAd(true); // 代表是广告
        adList.add(adDomain5);

        return adList;
    }
}
