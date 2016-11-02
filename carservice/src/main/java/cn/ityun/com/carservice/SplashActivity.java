package cn.ityun.com.carservice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class SplashActivity extends BaseActivity {
    private ImageView splash;
    private Animation animation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = (ImageView) findViewById(R.id.iv_splash);//找到需要旋转的控件
        startAnimation();//启动动漫（一张图片的旋转）
    }

    private void startAnimation() {
        animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        splash.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                if (sp.getString("name", "").equals("name")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.overridePendingTransition(R.anim.in_anim,R.anim.out_anim);
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    SplashActivity.this.overridePendingTransition(R.anim.in_anim,R.anim.out_anim);
                    finish();//结束当前页面
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
       /* splash.setAnimation(animation);
        animation.start();*/
    }


}
