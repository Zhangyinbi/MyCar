package cn.ityun.com.carservice.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.ityun.com.carservice.R;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class HomeFragment extends BaseFragment {


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.home_fragment, null);
        ListView lv = (ListView) view.findViewById(R.id.lv_news_list);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view1 = View.inflate(mActivity, R.layout.news_item, null);
                return view1;
            }
        });
lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("------", "onItemClick: " );

    }
});

        return view;
    }

}
