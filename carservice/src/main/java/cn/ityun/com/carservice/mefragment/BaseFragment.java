package cn.ityun.com.carservice.mefragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ityun.com.carservice.LoadActivity;
import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.R;

/**
 * 基类BaseFragment,所有Fragment继承此类
 * 1.定义Activity变量，方便自类使用
 * 2.定义抽象方法initView() 初始化布局，必须实现
 * 3.定义方法initData()，初始化数据，可以不实现
 * Created by Administrator on 2016/2/4 0004.
 */
public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    public LoadActivity mainUi;
    //fragment创建

    /*public BaseFragment(Activity mActivity) {
        Window window=mActivity.getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
           *//* //透明导航栏,导航栏的设置和上面一样
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*//*
        }else if (Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mainUi = (LoadActivity) mActivity;
       /* Window window=mActivity.getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
            //透明导航栏,导航栏的设置和上面一样
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }else if (Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
    }

    //处理fragment的布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    /**
     * 初始化布局
     *
     * @return
     */
    public abstract View initView();

    /**
     * Fragment所依赖的Activity创建完成
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

    /*public void jumpFragment(Fragment fragment, boolean flag) {
        //传进来的fragment根据需要携带参数
        FragmentTransaction ft = mainUi.fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        *//*
                        * 这个地方往查询结果界面进行跳转
                        *需要携带参数,注意携带参数的类型和种类,使用setArguement()
                        *//*

        ft.replace(R.id.fl_layout, fragment);


        if (flag) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }*/

    @Override
    public void onResume() {
        super.onResume();
    }
}
