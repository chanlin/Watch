package com.jajale.watch.utils;

import com.jajale.watch.R;

/**
 * Created by lilonghui on 2015/12/3.
 * Email:lilonghui@bjjajale.com
 */
public class ParentHeadViewLocationUtils {
    public static String[] mName = {"爸爸", "妈妈", "爷爷", "奶奶", "姥爷", "姥姥", "哥哥", "姐姐", "其他"};
    public static int[] mHeadView = {R.mipmap.head_image_location_father, R.mipmap.head_image_location_mother, R.mipmap.head_image_location_grandfather
            , R.mipmap.head_image_location_grandmother, R.mipmap.head_image_location_grandfather_mom, R.mipmap.head_image_location_grandmother_mom
            , R.mipmap.head_image_location_brother, R.mipmap.head_image_location_sister, R.mipmap.head_image_location_other};
    public static int[] mHeadView_press = {R.mipmap.head_image_father_press, R.mipmap.head_image_mother_press, R.mipmap.head_image_grandfather_press
            , R.mipmap.head_image_grandmother_press, R.mipmap.head_image_grandfather_mom_press, R.mipmap.head_image_grandmother_mom_press,
            R.mipmap.head_image_brother_press, R.mipmap.head_image_sister_press, R.mipmap.head_image_other_press};

    public static int getImage(String name) {
        for (int i = 0; i < mName.length; i++) {
            if (name.equals(mName[i])) {
                return mHeadView[i];
            }
        }
        return mHeadView[mName.length - 1];

    }
    public static int getImage_press(String name) {
        for (int i = 0; i < mName.length; i++) {
            if (name.equals(mName[i])) {
                return mHeadView_press[i];
            }
        }
        return mHeadView_press[mName.length - 1];

    }


}
