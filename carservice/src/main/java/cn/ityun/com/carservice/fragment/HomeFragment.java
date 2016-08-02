package cn.ityun.com.carservice.fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.homefragment.UsedCarFragment;
import cn.ityun.com.carservice.homefragment.ViolationCarListFragment;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.Utils;
import cn.ityun.com.carservice.view.image.SmartImageView;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private ListView newsList;
    private ImageView ivCheck;
    private ImageView ivGas;
    private ImageView ivClear;
    private ImageView ivHightSpeed;
    private RelativeLayout rlCars;
    private RelativeLayout rlService;
    private TextView tvLoadMore;
    public AMapLocationClient mLocationClient = MyApplication.get();
    private SmartImageView ivPic;
    private TextView tvWeek;
    private TextView tvWeather;
    private TextView tvTemp;
    private TextView tvUpData;
    private SharedPreferences sp;

    @Override
    public View initView() {
        sp = mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        View view = View.inflate(mActivity, R.layout.home_fragment, null);
        newsList = (ListView) view.findViewById(R.id.lv_news_list);
        ivCheck = (ImageView) view.findViewById(R.id.iv_check);
        ivCheck.setOnClickListener(this);
        ivGas = (ImageView) view.findViewById(R.id.iv_gas);
        ivGas.setOnClickListener(this);
        ivClear = (ImageView) view.findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(this);
        ivHightSpeed = (ImageView) view.findViewById(R.id.iv_road);
        ivHightSpeed.setOnClickListener(this);
        rlCars = (RelativeLayout) view.findViewById(R.id.rl_cars);
        rlCars.setOnClickListener(this);
        rlService = (RelativeLayout) view.findViewById(R.id.rl_service);
        rlService.setOnClickListener(this);
        tvLoadMore = (TextView) view.findViewById(R.id.tv_loadMore);
        tvLoadMore.setOnClickListener(this);
        ivPic = (SmartImageView) view.findViewById(R.id.iv_pic);
        tvWeek = (TextView) view.findViewById(R.id.tv_week);
        tvWeather = (TextView) view.findViewById(R.id.tv_weather);
        tvTemp = (TextView) view.findViewById(R.id.tv_temp);
        tvUpData = (TextView) view.findViewById(R.id.tv_upData);
        ivPic.setImageUrl(sp.getString("weather_pic",""));
        tvWeek.setText(sp.getString("week",""));
        tvWeather.setText(sp.getString("weather",""));
        tvUpData.setText(sp.getString("updatetime",""));
        tvTemp.setText(sp.getString("wendu",""));
        MyApplication.setOnLocation(new MyApplication.OnLocation() {
            @Override
            public void setData(AMapLocationClient mLocationClient, AMapLocation amapLocation) {
                HomeFragment.this.mLocationClient = mLocationClient;
                Log.e("666666666666666666", amapLocation.getAddress() );
                RequestQueue queues = MyApplication.queues;
                String url = "http://route.showapi.com/9-2?showapi_appid=20576&&showapi_sign=31d63c00b46841a8811483c04a4c7e56&&area=" + amapLocation.getCity();
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
                            JSONObject showapi_res_body = json.getJSONObject("showapi_res_body");
                            JSONObject now = showapi_res_body.getJSONObject("now");
                            String weather_pic = now.getString("weather_pic");
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("weather_pic",weather_pic);
                            edit.commit();
                            ivPic.setImageUrl(weather_pic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Utils.showToast(mActivity,"天气加载失败");
                    }
                });
                String city = amapLocation.getCity();
                String cc=city.substring(0,city.length()-1);
                String url2="http://api.jisuapi.com/weather/query?appkey=4e3ada1ff0498822&city="+cc;
                StringRequest quest=new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json=new JSONObject(s);
                            JSONObject result = json.getJSONObject("result");
                            tvWeek.setText(result.getString("week"));
                            tvWeather.setText(result.getString("weather"));
                            tvTemp.setText(result.getString("templow")+"~"+result.getString("temphigh")+"℃");
                            tvUpData.setText(result.getString("updatetime")+"更新");
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("wendu",result.getString("templow")+"~"+result.getString("temphigh")+"℃");
                            edit.putString("week",result.getString("week"));
                            edit.putString("weather",result.getString("weather"));
                            edit.putString("updatetime",result.getString("updatetime")+"更新");
                            edit.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Utils.showToast(mActivity,"天气加载失败");
                    }
                });

                queues.add(quest);
                queues.add(request);
            }
        });
        /*lv.setAdapter(new BaseAdapter() {
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
                Log.e("------", "onItemClick: ");
            }
        });*/

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_check:
                getFragment(1);
                break;
            case R.id.iv_gas:
                getFragment(2);
                break;
            case R.id.iv_clear:
                getFragment(3);
                break;
            case R.id.rl_cars:
                getFragment(4);
                break;
            case R.id.iv_road:
                getFragment(5);
                break;
            case R.id.rl_service:
                getFragment(6);
                break;
            case R.id.tv_loadMore:
                getFragment(7);
                break;
        }
    }

    private void getFragment(int i) {

        mainUi.rGroup.setVisibility(View.GONE);
        FragmentTransaction ft = mainUi.fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (i == 1) {
            ft.replace(mainUi.flLayout.getId(), new ViolationCarListFragment());
        }
        if (i == 2) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }
        if (i == 3) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }
        if (i == 4) {
            ft.replace(mainUi.flLayout.getId(), new UsedCarFragment());
        }
        if (i == 5) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }
        if (i == 6) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }
        if (i == 7) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }

        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();

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
    public void onDestroy() {
        super.onDestroy();
    }
}
