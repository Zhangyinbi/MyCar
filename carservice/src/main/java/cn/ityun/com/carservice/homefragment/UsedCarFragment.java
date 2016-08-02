package cn.ityun.com.carservice.homefragment;

import android.graphics.Color;
import android.view.View;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class UsedCarFragment extends BaseFragment {
    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.used_car_fragment,null);



        return mainUi.show(view, Color.parseColor("#0f97ee"));
    }
}
