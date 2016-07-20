package cn.ityun.com.carservice;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class Utils {
    public static int ScreenHight(Activity activity){
            Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics=new DisplayMetrics();
            display.getMetrics(outMetrics);
            return outMetrics.heightPixels;
    }
}
