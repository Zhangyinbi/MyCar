package cn.ityun.com.carservice.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ityun.com.carservice.LoadActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.bean.FindInfo;
import cn.ityun.com.carservice.view.CircleImageView;
import cn.ityun.com.carservice.view.RefreshListView;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class MeFragment extends BaseFragment {
    private ArrayList<FindInfo> list;
    private RefreshListView lvList;
    private CircleImageView ivHeadPic;
    private String TAG = "---------------";
    private TextView tvLogin;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.me_fragment, null);
        lvList = (RefreshListView) view.findViewById(R.id.lv_list);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("=======", "发现 " + position);
            }
        });
        ivHeadPic = (CircleImageView) view.findViewById(R.id.iv_head_pic);
        ivHeadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "ivHeadPic: ");
                //修改本人信息点击
            }
        });

        tvLogin = (TextView) view.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, LoadActivity.class);
                startActivity(intent);
//                mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        list = new ArrayList<FindInfo>();
        list.add(new FindInfo(R.mipmap.my_car, "我的车辆", ""));
        list.add(new FindInfo(R.mipmap.my_collect, "我的收藏", ""));
        list.add(new FindInfo(R.mipmap.my_publish, "我发表的", ""));
        list.add(new FindInfo(R.mipmap.recommend, "推荐给朋友", ""));
        list.add(new FindInfo(R.mipmap.setting, "设置", ""));
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
                    convertView = View.inflate(mActivity, R.layout.me_list_item, null);
                    myholder = new ViewHolder();
                    myholder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                    myholder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    convertView.setTag(myholder);
                } else {
                    myholder = (ViewHolder) convertView.getTag();
                }
                myholder.ivPic.setImageResource(list.get(position).getImageId());
                myholder.tvName.setText(list.get(position).getName());

                return convertView;
            }
        });

    }

    private class ViewHolder {
        private ImageView ivPic;
        private TextView tvName;
    }
}
