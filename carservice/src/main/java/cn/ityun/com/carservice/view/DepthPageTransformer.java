package cn.ityun.com.carservice.view;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 *
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 0.45f;

    @Override
   public void transformPage(View view, float position) {
        Log.e("-----", "transformPage" );
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-one)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-one,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1+position);
            view.setTranslationX(0);
            view.setScaleX(Math.max(0.3f,1+position));
            view.setScaleY(Math.max(0.3f,1+position));

        } else if (position <= 1) { // (0,one]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and one)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (one,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}