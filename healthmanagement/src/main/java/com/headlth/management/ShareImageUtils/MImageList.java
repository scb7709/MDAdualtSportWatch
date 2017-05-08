package com.headlth.management.ShareImageUtils;

import android.app.Activity;

import java.util.List;

/**
 * Created by abc on 2016/9/6.
 */
public class MImageList  {
    private static MImageList MImageList;
    private static Activity context;
    private static List<ImageModel> mImageList;//相册图片
    private MImageList() {
    }

    public static MImageList getInstance(Activity activity) {

        if (MImageList == null) {
            synchronized (MImageList.class) {
                if (MImageList == null) {
                    MImageList = new MImageList();
                    mImageList=Utils.getImages(activity);
                }

            }
        }
        return MImageList;
    }

    public  List<ImageModel> getMImageList() {
        return mImageList;
    }
}
