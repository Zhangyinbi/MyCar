package cn.ityun.com.carservice.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.bean.FindInfo;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.Utils;
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
    public AMapLocationClient mLocationClient = MyApplication.get();
    public static String province="";
    private SharedPreferences sp;

    @Override
    public View initView() {
        sp = mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        View view = View.inflate(mActivity, R.layout.find_fragment, null);
        MainActivity mainUI = (MainActivity) mActivity;

        lvList = (RefreshListView) view.findViewById(R.id.lv_list);


        tvCity= (TextView) view.findViewById(R.id.tv_city);
        tv90Price= (TextView) view.findViewById(R.id.tv_90_price);
        tv97Price= (TextView) view.findViewById(R.id.tv_97_price);
        tv93Price= (TextView) view.findViewById(R.id.tv_93_price);
        tv0Price= (TextView) view.findViewById(R.id.tv_0_price);

        MyApplication.setOnLocation(new MyApplication.OnLocation() {
            @Override
            public void setData(AMapLocationClient mLocationClient, AMapLocation amapLocation) {
                province=amapLocation.getProvince();
                province=province.substring(0,province.length()-1);
                String url = "https://route.showapi.com/138-46?showapi_appid=20576&showapi_sign=31d63c00b46841a8811483c04a4c7e56&prov=" + province;
                Log.e("-----------", url );
                RequestQueue queues = MyApplication.getHttpQueues();
                StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("---------", "onResponse: "+s );
                        try {
                            JSONObject json=new JSONObject(s);
                            JSONObject jsonObject = json.getJSONObject("showapi_res_body").getJSONArray("list").getJSONObject(0);
                            tvCity.setText(province);
                            tv90Price.setText(jsonObject.getString("p90"));
                            tv97Price.setText(jsonObject.getString("p97"));
                            tv93Price.setText(jsonObject.getString("p93"));
                            tv0Price.setText(jsonObject.getString("p0"));
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("p0",jsonObject.getString("p0"));
                            edit.putString("province",province);
                            edit.putString("p90",jsonObject.getString("p90"));
                            edit.putString("p97",jsonObject.getString("p97"));
                            edit.putString("p93",jsonObject.getString("p93"));
                            edit.commit();
                        } catch (JSONException e) {
                            Utils.showToast(mActivity,"刷新油价失败");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Utils.showToast(mActivity,"刷新油价失败");
                    }
                });
                queues.add(request);
            }
        });
        return mainUI.show(view, Color.parseColor("#0099ff"));
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
//        Log.e("---------666666----", "回来了: " );
        mainUi.rGroup.setVisibility(View.VISIBLE);
        if (mLocationClient !=null ) {
            mLocationClient.startLocation();
        }
    }
    @Override
    public void initData() {
        list = new ArrayList<FindInfo>();
        tvCity.setText(sp.getString("province",""));
        tv90Price.setText(sp.getString("p90",""));
        tv97Price.setText(sp.getString("p97",""));
        tv93Price.setText(sp.getString("p93",""));
        tv0Price.setText(sp.getString("p0",""));
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
