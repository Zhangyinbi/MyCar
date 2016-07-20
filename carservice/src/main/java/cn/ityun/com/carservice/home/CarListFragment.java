package cn.ityun.com.carservice.home;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;

import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class CarListFragment extends BaseFragment {
    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.violation_fragment,null);
        MainActivity mainUI = (MainActivity) mActivity;
        ImageButton ivBack= (ImageButton) view.findViewById(R.id.ib_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return mainUI.show(view, Color.parseColor("#0f97ee"));
    }
}
