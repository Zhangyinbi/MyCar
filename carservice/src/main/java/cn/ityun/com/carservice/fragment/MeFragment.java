package cn.ityun.com.carservice.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ityun.com.carservice.LoadActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.bean.FindInfo;
import cn.ityun.com.carservice.global.DataInterface;
import cn.ityun.com.carservice.mefragment.MeInfoSettingFragment;
import cn.ityun.com.carservice.mefragment.PostFragment;
import cn.ityun.com.carservice.utils.Utils;
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
    private TextView tvUseName;
    boolean flag = false;
    private SharedPreferences sp;

    @Override
    public View initView() {
        sp = mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        View view = View.inflate(mActivity, R.layout.me_fragment, null);
        lvList = (RefreshListView) view.findViewById(R.id.lv_list);
        tvUseName = (TextView) view.findViewById(R.id.tv_use_name);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    mainUi.rGroup.setVisibility(View.GONE);
                    jumpFragment(new PostFragment(), true);
                }
                else {
                    Utils.showToast(mActivity,"别乱点");
                }
            }
        });
        ivHeadPic = (CircleImageView) view.findViewById(R.id.iv_head_pic);
        ivHeadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改本人信息点击
                //flag  是否处于登陆状态
                if (flag) {
                    mainUi.rGroup.setVisibility(View.GONE);
                    jumpFragment(new MeInfoSettingFragment(), true);
                } else {
                    Utils.showToast(mActivity, "点先登录");
                }
            }
        });

        tvLogin = (TextView) view.findViewById(R.id.tv_login);


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
//        mainUi.rGroup.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainUi.rGroup.setVisibility(View.VISIBLE);
        flag = sp.getBoolean("flag", false);
        Log.e(TAG, flag + "");
        if (flag) {
            tvLogin.setVisibility(View.GONE);
            tvUseName.setVisibility(View.VISIBLE);
            tvUseName.setText(sp.getString("nickname", "ooo"));
//            Uri uri=Uri.parse(DataInterface.SERVER_UPLOAD+"upload/"+sp.getString("headPic","ooo"));
//            Log.e(TAG, DataInterface.SERVER_UPLOAD+"upload/"+sp.getString("headPic","ooo") );
            ivHeadPic.setImageUrl(DataInterface.SERVER_UPLOAD + "upload/" + sp.getString("headPic", "ooo"), R.mipmap.head_pic);
//            Log.e(TAG,sp.getString("nickname","ooo"));
//            ivHeadPic.setClickable(true);
        } else {
            tvLogin.setVisibility(View.VISIBLE);
            tvUseName.setVisibility(View.GONE);
//            ivHeadPic.setClickable(false);
        }
    }

    @Override
    public void initData() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, LoadActivity.class);
                startActivity(intent);
//                mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
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
