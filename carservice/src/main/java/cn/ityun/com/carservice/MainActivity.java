package cn.ityun.com.carservice;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.ityun.com.carservice.fragment.BaseFragment;
import cn.ityun.com.carservice.fragment.FindFragment;
import cn.ityun.com.carservice.fragment.HomeFragment;
import cn.ityun.com.carservice.fragment.MeFragment;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.MyDbOpenHelper;
import cn.ityun.com.carservice.utils.Utils;


public class MainActivity extends BaseActivity {

    public FrameLayout flLayout;
    public RadioGroup rGroup;
    private RadioButton rbHome;
    private RadioButton rbFind;
    private RadioButton rbMe;
    public String car = "";
    private ArrayList<BaseFragment> mPagerlist;
    HomeFragment one;
    FindFragment two;
    MeFragment three;
    FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*View contentView=View.inflate(this,R.layout.activity_main,null);
        LinearLayout ll_content = new LinearLayout(this);
        ll_content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_content.setOrientation(LinearLayout.VERTICAL);
        addStatusBar(ll_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll_content.addView(contentView,layoutParams);*/
        setContentView(R.layout.activity_main);
        new MyDbOpenHelper(this);
        initView();
        initData();

    }

    private void initData() {
        mPagerlist = new ArrayList<BaseFragment>();
        rGroup.check(R.id.rb_home_bottom);
    }


    /**
     * 初始化控件
     */
    private void initView() {
        flLayout = (FrameLayout) findViewById(R.id.fl_layout);
        rGroup = (RadioGroup) findViewById(R.id.rg_group);
        rbHome = (RadioButton) findViewById(R.id.rb_home_bottom);
        rbFind = (RadioButton) findViewById(R.id.rb_find_bottom);
        rbMe = (RadioButton) findViewById(R.id.rb_me_bottom);

        //设置监听
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home_bottom:
                        getFragment(1);
                        break;
                    case R.id.rb_find_bottom:
                        getFragment(2);
                        break;
                    case R.id.rb_me_bottom:
                        getFragment(3);
                        break;
                }
            }
        });
    }

    int j = 0;

    private void getFragment(int i) {
        ft = fm.beginTransaction();
        hideFragment(ft);//隐藏所有的fragment
        if (j >= 2) {
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        j++;
        if (i == 1) {
            if (one == null) {
                one = new HomeFragment();
//                mPagerlist.add(one);
                ft.add(R.id.fl_layout, one);
            } else {
                ft.show(one);
            }
        }
        if (i == 2) {
            if (two == null) {
                two = new FindFragment();
                ft.add(R.id.fl_layout, two);
//                mPagerlist.add(two);
            } else {
                ft.show(two);
            }
        }
        if (i == 3) {
            if (three == null) {
                three = new MeFragment();
                ft.add(R.id.fl_layout, three);
//                mPagerlist.add(three);
            } else {
                ft.show(three);
            }
        }
        ft.commit();
    }

    // 隐藏所有的Fragment
    public void hideFragment(FragmentTransaction transaction) {
//        Log.e(TAG, "hideFragment: " );
        if (one != null) {
            if (one.isVisible()) {
                transaction.hide(one);
            }
//            Log.e(TAG, "hideFragment: one" );

        }
        if (two != null) {
//            Log.e(TAG, "hideFragment: two" );
            if (two.isVisible()) {

                transaction.hide(two);
            }
        }
        if (three != null) {
//            Log.e(TAG, "hideFragment: three" );
            if (three.isVisible()) {

                transaction.hide(three);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    boolean flag = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            flag = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (one != null && one.isVisible()||two != null && two.isVisible()||three != null && three.isVisible()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!flag) {
                    Utils.showToast(this, "再点一次退出");
                    flag = true;
                    handler.sendEmptyMessageDelayed(0, 2000);
                } else {
                    finish();
                }
            }
        }else {
            super.onKeyDown(keyCode,event);
        }
        return true;
    }
}
