package cn.ityun.com.carservice.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.bean.FindInfo;
import cn.ityun.com.carservice.view.RefreshListView;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class FindFragment extends BaseFragment {
    private ArrayList<FindInfo> list;
    private RefreshListView lvList;
    private TextView tvCity;
    private TextView tv90Price;
    private TextView tv93Price;
    private TextView tv97Price;
    private TextView tv0Price;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.find_fragment, null);
        MainActivity mainUI = (MainActivity) mActivity;
        lvList = (RefreshListView) view.findViewById(R.id.lv_list);
        tvCity= (TextView) view.findViewById(R.id.tv_city);
        tv90Price= (TextView) view.findViewById(R.id.tv_90_price);
        tv97Price= (TextView) view.findViewById(R.id.tv_97_price);
        tv93Price= (TextView) view.findViewById(R.id.tv_93_price);
        tv0Price= (TextView) view.findViewById(R.id.tv_0_price);

        return mainUI.show(view, Color.parseColor("#0099ff"));
    }

    @Override
    public void initData() {
        list = new ArrayList<FindInfo>();
        list.add(new FindInfo(R.mipmap.friend_circle, "车友圈", "看看车友都在干什么"));
        list.add(new FindInfo(R.mipmap.help, "挪车助手", "一键呼叫，烦恼解决"));
        list.add(new FindInfo(R.mipmap.autocar, "汽车咨询", "最新的新闻咨询"));
        lvList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public FindInfo getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder myholder;
                if (convertView == null) {
                    convertView = View.inflate(mActivity, R.layout.find_list_item, null);
                    myholder = new ViewHolder();
                    myholder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                    myholder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    myholder.tvDetail = (TextView) convertView.findViewById(R.id.tv_detail);
                    convertView.setTag(myholder);
                } else {
                    myholder = (ViewHolder) convertView.getTag();
                }
                myholder.ivPic.setImageResource(list.get(position).getImageId());
                myholder.tvName.setText(list.get(position).getName());
                myholder.tvDetail.setText(list.get(position).getDetail());

                return convertView;
            }
        });

    }

    private class ViewHolder {
        private ImageView ivPic;
        private TextView tvName;
        private TextView tvDetail;
    }


    /*手动添加小圆点
        *liear = (LinearLayout) view.findViewById(R.id.id_linear);
         point();*/
   /* private void point() {

        ImageView v = new ImageView(mActivity);
        v.setBackgroundResource(R.drawable.bt1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
        v.setLayoutParams(params);
        liear.addView(v);


    }*/
}
