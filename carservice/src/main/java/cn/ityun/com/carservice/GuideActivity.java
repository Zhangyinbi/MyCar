package cn.ityun.com.carservice;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ityun.com.carservice.view.DepthPageTransformer;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class GuideActivity extends BaseActivity {
    private ViewPager mViewPager;
    private int[] mImgIds = new int[]{R.mipmap.guide_image1, R.mipmap.guide_image2, R.mipmap.guide_image3, R.mipmap.guide_image4};
    private List<ImageView> mImages = new ArrayList<ImageView>();
    private int mScreenHeight;

    @TargetApi(Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
        initData();
        //为ViewPager添加动画效果,只有3.0以上有效果，想要兼容，需要重新ViewPager
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImgIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(GuideActivity.this);
                imageView.setImageResource(mImgIds[position]);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                int y = (int) event.getRawY();
                                if (y >= mScreenHeight * (0.925)) {
                                    if (mViewPager.getCurrentItem() == 3) {
//                                        Log.e("-----", "===" );
                                        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                                        GuideActivity.this.startActivity(intent);
                                        GuideActivity.this.overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
                                        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("name", "name");
                                        editor.commit();
                                        SharedPreferences sp1 = getSharedPreferences("userinfo", MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sp1.edit();
                                        edit.putString("username", "");
                                        edit.putString("password", "");
                                        edit.putBoolean("flag", false);
                                        edit.commit();
                                        finish();
                                    } else {
//                                        Log.e("-----", "555555555" );
                                        ScaleAnimation sa = new ScaleAnimation(0f, 1f, 0f, 1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                                        AlphaAnimation aa = new AlphaAnimation(0f, 1f);
                                        aa.setDuration(2000);
                                        sa.setDuration(2000);
                                        AnimationSet set = new AnimationSet(false);
                                        set.addAnimation(aa);
                                        set.addAnimation(sa);
                                        // sa.setFillAfter(true);
                                        // mImages.get(mViewPager.getCurrentItem()+one).startAnimation(sa);
                                        mViewPager.setAnimation(set);
                                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });
                container.addView(imageView);
                mImages.add(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImages.get(position));
            }
        });

        /*mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        int y= (int) event.getRawY();
                        if (y>=mScreenHeight*(0.925)){
                            int i = v.getId() + one;
                            if (i==3){
                                Toast.makeText(GuideActivity.this,"---",Toast.LENGTH_LONG).show();
                                break;
                            }
                            mViewPager.setCurrentItem(i);
                        }
                        break;
                }
                return true;
            }
        });*/
    }


    private void initData() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
    }


}
