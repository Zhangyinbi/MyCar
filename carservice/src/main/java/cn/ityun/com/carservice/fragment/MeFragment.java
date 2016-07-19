package cn.ityun.com.carservice.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class MeFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView text=new TextView(mActivity);
        text.setText("我的");
        text.setTextColor(Color.parseColor("#ff0000"));
        text.setGravity(Gravity.CENTER);
        return text;
    }
}
