package cn.ityun.com.carservice.homefragment.violation;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.bean.gsonbean.IllegaInfo;
import cn.ityun.com.carservice.fragment.BaseFragment;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.MyDbOpenHelper;
import cn.ityun.com.carservice.view.RefreshListView;

/**
 * Created by Administrator on 2016/7/23 0023.
 */
public class IllegalInfoListFragment extends BaseFragment {

    private ImageView btn;
    private LinearLayout linearLayout;
    private TextView tvCarNum;
    private TextView tvCarType;
    private TextView tv_load;
    private Bundle bundle;
    private LinearLayout ll_loading;
    private RelativeLayout rl_suss;
    private TextView tv_count;
    private TextView tv_msg;
    private RefreshListView lv_list;
    private TextView tv_score;
    private TextView tv_price;
    private View v;
    private ArrayList<IllegaInfo.info.jutiInfo> lists;

    @Override
    public View initView() {
        bundle = getArguments();
        final View view = View.inflate(mActivity, R.layout.illegalinfolst_fragment, null);
        v = view.findViewById(R.id.v);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        lv_list = (RefreshListView) view.findViewById(R.id.lv_info_list);


        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        btn = (ImageView) view.findViewById(R.id.btn_back);
        tvCarNum = (TextView) view.findViewById(R.id.tv_car_num);
        tvCarType = (TextView) view.findViewById(R.id.tv_car_type);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll_info);
        tv_load = (TextView) view.findViewById(R.id.tv_load);
        ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);
        rl_suss = (RelativeLayout) view.findViewById(R.id.rl_suss);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = 89+mainUi.getStatusBarHight();
        linearLayout.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = mainUi.getStatusBarHight();
        btn.setLayoutParams(lp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        String url = "http://api.jisuapi.com/illegal/query?appkey=4e3ada1ff0498822&lsprefix=" + bundle.getString("lsprefix") + "&lsnum=" + bundle.getString("lsnum") + "&lstype=" + bundle.getString("lstype") + "&frameno=" + bundle.getString("frameno") + "&carorg=" + bundle.getString("carorg");
        RequestQueue queues = MyApplication.queues;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        ll_loading.setVisibility(View.GONE);
                        rl_suss.setVisibility(View.VISIBLE);
                        tvCarNum.setText(bundle.getString("lsprefix") + bundle.getString("lsnum"));
                        tvCarType.setText(bundle.getString("type"));
                        parseData(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                tv_load.setText("加载失败");
            }
        });
        queues.add(request);
        return view;
    }
    String msg = "";
    private void parseData(String s) {
        Log.e("----------", s);
        int score = 0;
        int price = 0;
        Gson gson = new Gson();
        try {
            JSONObject json = new JSONObject(s);
            msg = json.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (msg.equals(null) || msg.equals("")) {
            IllegaInfo carinfo = gson.fromJson(s, IllegaInfo.class);
            lists = carinfo.result.list;
            tv_count.setText(lists.size() + "");
            tv_msg.setVisibility(View.GONE);
            lv_list.setVisibility(View.VISIBLE);
            v.setVisibility(View.VISIBLE);
            for (IllegaInfo.info.jutiInfo jutiInfo : lists) {
                score = score + jutiInfo.score;
                price = price + jutiInfo.price;
            }
            lv_list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return lists.size();
                }

                @Override
                public IllegaInfo.info.jutiInfo getItem(int position) {
                    return lists.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = View.inflate(mActivity, R.layout.illega_list_item, null);
                    TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
                    TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
                    TextView tcContent = (TextView) view.findViewById(R.id.tc_content);
                    TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);
                    TextView tvScore = (TextView) view.findViewById(R.id.tv_score);
                    tvTime.setText(lists.get(position).time);
                    tvAddress.setText(lists.get(position).address);
                    tcContent.setText(lists.get(position).content);
                    tvPrice.setText(lists.get(position).price + "");
                    tvScore.setText(lists.get(position).score + "");
                    return view;
                }
            });
        } else {
            lv_list.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            tv_msg.setVisibility(View.VISIBLE);
            tv_msg.setText(msg);
            tv_count.setText(0 + "");
        }
        tv_price.setText(price + "");
        tv_score.setText(score + "");
        saveData();
    }

    private void saveData() {
        Cursor cursor = MyDbOpenHelper.dbHelper.rawQuery("select * from carinfo", null);
        ContentValues values = new ContentValues();
        while (cursor.moveToNext()) {
            String chepai = cursor.getString(cursor.getColumnIndex("chepai"));
            if (chepai != null) {
                if (chepai.equals(bundle.getString("lsprefix")+bundle.getString("lsnum"))) {
                    values.put("lsprefix", bundle.getString("lsprefix"));
                    values.put("lsnum", bundle.getString("lsnum"));
                    values.put("lstype", bundle.getString("lstype"));
                    values.put("frameno", bundle.getString("frameno"));
                    values.put("carorg", bundle.getString("carorg"));
                    values.put("type", bundle.getString("type"));
                    values.put("count",tv_count.getText().toString());
                    values.put("msg",msg);
                    values.put("price",tv_price.getText().toString());
                    values.put("score",tv_score.getText().toString());
                    values.put("time", System.currentTimeMillis());
                    MyDbOpenHelper.dbHelper.update("carinfo", values, "chepai=?", new String[]{chepai});
                    return;
                }
            }
        }
        values.put("msg",msg);
        values.put("chepai", bundle.getString("lsprefix")+bundle.getString("lsnum"));
        values.put("lsprefix", bundle.getString("lsprefix"));
        values.put("lsnum", bundle.getString("lsnum"));
        values.put("lstype", bundle.getString("lstype"));
        values.put("frameno", bundle.getString("frameno"));
        values.put("carorg", bundle.getString("carorg"));
        values.put("type", bundle.getString("type"));
        values.put("count",tv_count.getText().toString());
        values.put("price",tv_price.getText().toString());
        values.put("score",tv_score.getText().toString());
        values.put("time", System.currentTimeMillis());
        MyDbOpenHelper.dbHelper.insert("carinfo", null, values);

    }




}
