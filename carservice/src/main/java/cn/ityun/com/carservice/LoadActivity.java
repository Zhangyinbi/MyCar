package cn.ityun.com.carservice;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ityun.com.carservice.global.DataInterface;
import cn.ityun.com.carservice.mefragment.ForgetPasswordFragment;
import cn.ityun.com.carservice.mefragment.RegisterFragment;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.Utils;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class LoadActivity extends BaseActivity {
    private ImageView ivClose;
    private EditText etPhoneNum;
    private ImageView ivPhone;
    private ImageView ivPwd;
    private EditText etPassword;
    private ImageView ivIsShowPassword;
    private boolean flag = false;
    private TextView tvRegister;
    public static int i = 0;
    private Button btnLand;
    private TextView tvForgetPwd;
    private SensorManager sensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_load, null);
        View contentView = show(view, Color.parseColor("#0099ff"));
        setContentView(contentView);
        initView();
        initData();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null)
            sensorManager.unregisterListener(listener);
    }

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            /*if (event.values[0] > 2) {
                jumpFragment(new RegisterFragment());
            }
            if (event.values[0] < -2) {
                jumpFragment1(new ForgetPasswordFragment());
            }*/
            Log.e(TAG, "onSensorChanged: "+event.values[0]);

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 初始化控件内容  有的话直接赋值
     */
    private void initData() {
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        etPhoneNum.setText(sp.getString("username", ""));
        etPassword.setText(sp.getString("password", ""));
    }

    /**
     *
     */
    private void initView() {
        btnLand = (Button) findViewById(R.id.btn_land);
        ivPhone = (ImageView) findViewById(R.id.iv_phone);
        ivPwd = (ImageView) findViewById(R.id.iv_pwd);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v   叉号 右上角的监听  点击销毁当前Activity
             */
            @Override
            public void onClick(View v) {
                Log.e("------------", "onClick: ");
                finish();
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etPhoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * @param v   是否获取焦点，改变右侧小图标的明暗，就是两张图片切换
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Log.e(TAG, hasFocus + "");
                if (hasFocus) {
                    ivPhone.setImageResource(R.mipmap.telephone);
                } else {
                    ivPhone.setImageResource(R.mipmap.telephone_gray);
                }
            }
        });
        etPassword = (EditText) findViewById(R.id.et_password);
        ivIsShowPassword = (ImageView) findViewById(R.id.iv_isShow_password);
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * @param v     是否获取焦点，改变右侧小图标的明暗，就是两张图片切换
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                Log.e(TAG, hasFocus + "");
                if (hasFocus) {
                    ivPwd.setImageResource(R.mipmap.password);
//                    ivIsShowPassword.setImageResource(R.mipmap.eye_close);
                } else {
                    ivPwd.setImageResource(R.mipmap.password_gray);

                }
            }
        });
        ivIsShowPassword.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v  明文暗文监听   密码是否可见
             */
            @Override
            public void onClick(View v) {
                if (!flag) {
                    ivIsShowPassword.setImageResource(R.mipmap.eye_gray);
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    ivIsShowPassword.setImageResource(R.mipmap.eye_open_gray);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                flag = !flag;
            }
        });
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v  点击注册监听
             */
            @Override
            public void onClick(View v) {
                jumpFragment(new RegisterFragment());
            }
        });
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v  点击忘记密码的监听
             */
            @Override
            public void onClick(View v) {
                jumpFragment1(new ForgetPasswordFragment());
            }
        });


        btnLand.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v  登陆的监听
             */
            @Override
            public void onClick(View v) {
                final String phonenum = etPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phonenum) || phonenum.length() != 11) {
//                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    Utils.showToast(getApplication(), "请输入正确的手机号");
                    return;
                }
                final String pwd = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
//                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    Utils.showToast(getApplication(), "请输入密码");
                    return;
                }
                String url = DataInterface.SERVER_UPLOAD + "userAction_loginUser.action?phone=" + phonenum + "&password=" + pwd;
//                Log.e(TAG,url );
                RequestQueue queues = MyApplication.queues;
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
//                            Log.e(TAG,s );
                            if (json.getInt("code") == 0) {
                                JSONObject data = json.getJSONObject("data");
                                SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("username", phonenum);
                                edit.putString("password", pwd);
                                edit.putBoolean("flag", true);
                                edit.putInt("id", data.getInt("id"));
                                edit.putString("nickname", data.getString("userName"));
                                edit.putString("headPic", data.getString("headPic"));
                                edit.commit();
                                finish();
                            } else {
                                Toast.makeText(getApplication(), json.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getApplication(), "登录失败", Toast.LENGTH_SHORT).show();
                            Utils.showToast(getApplication(), "登录失败");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Utils.showToast(getApplication(), "登录失败");
                    }
                });
                queues.add(request);
            }
        });


    }

    public void jumpFragment(Fragment fragment) {
        //传进来的fragment根据需要携带参数
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit);
        /*
        * 这个地方往查询结果界面进行跳转
        *需要携带参数,注意携带参数的类型和种类,使用setArguement()
        */
        ft.replace(R.id.fl_layout, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    public void jumpFragment1(Fragment fragment) {
        //传进来的fragment根据需要携带参数
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(R.animator.fragment_slide_right_enter, R.animator.fragment_slide_right_exit, R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit);
        /*
        * 这个地方往查询结果界面进行跳转
        *需要携带参数,注意携带参数的类型和种类,使用setArguement()
        */
        ft.replace(R.id.fl_layout, fragment);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }


}
