package cn.ityun.com.carservice;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class BaseActivity extends Activity{
    public String TAG="-------------";

    private View mStatusBar;
    public FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window=getWindow();
        fm = getFragmentManager();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
           /* //透明导航栏,导航栏的设置和上面一样
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        }else if (Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
      /*
       *获取手机版本的其中一种方法
       *int version = android.provider.Settings.System.getInt(this.getContentResolver(),
                android.provider.Settings.System.SYS_PROP_SETTING_VERSION,
                3);*/

    }

    public int getStatusBarHight(){
        int result=0;
        int resourceId=getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId>0){
            result=getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public void addStatusBar(ViewGroup viewGroup,int i){
        mStatusBar = new View(this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHight());
        mStatusBar.setLayoutParams(lp);
        mStatusBar.setBackgroundColor(i);
        viewGroup.addView(mStatusBar);

    }
    public View show(View view,int i){
        LinearLayout ll_content = new LinearLayout(this);
        ll_content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_content.setOrientation(LinearLayout.VERTICAL);
        addStatusBar(ll_content,i);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll_content.addView(view,layoutParams);
        return ll_content;
    }

}
