package cn.ityun.com.carservice.mefragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.global.DataInterface;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.Utils;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class ForgetPasswordFragment extends BaseFragment {

    private TextView ivBack;
    private EditText etPhoneNum;
    private TextView tvCode;
    int count = 59;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg.what == 1) {
                tvCode.setText("重新获取");
                tvCode.setTextColor(Color.parseColor("#cccccc"));
                tvCode.setClickable(true);
                mHandler.removeMessages(0);
                String result = (String) msg.obj;
                if (result == null) {
                    Utils.showToast(mActivity,"获取验证码失败，请重新获取");
//                    Toast.makeText(mActivity, "获取验证码失败，请重新获取", Toast.LENGTH_SHORT).show();
                    count = 59;
                    return;
                }
//                    Log.e("========", result);
                Utils.showToast(mActivity,result);
//                Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();
                count = 59;
                return;
            }
            if (msg.what == 2) {
                Utils.showToast(mActivity,"验证码已发送至您手机，请注意查收");
//                Toast.makeText(mActivity, "验证码已发送至您手机，请注意查收", Toast.LENGTH_SHORT).show();
                return;
            }

            if (msg.what == 3) {
                //注册成功之后的操作，把账号密码上传至服务器，并在本地保存一份，切设置一个状态用来
                //记录当前是否是登录状态，在记录一个开始时间，以后每次登陆也记录一个时间，当时间间隔相差15天时
                //让用户重新登陆
                llNext.setVisibility(View.GONE);
                llSure.setVisibility(View.VISIBLE);
                tv.setTextColor(Color.parseColor("#0f97ee"));
            }
            tvCode.setText(count-- + "s");
            tvCode.setTextColor(Color.parseColor("#ee000000"));
            mHandler.sendEmptyMessageDelayed(0, 1000);
            if (count == 0) {
                tvCode.setText("重新获取");
                tvCode.setTextColor(Color.parseColor("#cccccc"));
                mHandler.removeMessages(0);
                count = 59;
                tvCode.setClickable(true);
            }
        }
    };

    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    //去做注册操作
                    mHandler.sendEmptyMessage(3);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    //弹出toast 提示用户查看手机
                    mHandler.sendEmptyMessage(2);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
//                Log.e("=============", ((Throwable) data).getMessage() );
                Message message = Message.obtain();
                String resu = ((Throwable) data).getMessage();
                try {
                    JSONObject json = new JSONObject(resu);
                    message.obj = json.getString("detail");
                    message.what = 1;
//                    Log.e("===========json==", json.getString("detail") );
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);
                }

            }
        }
    };
    private Button btnNext;
    private EditText etCode;
    private LinearLayout llNext;
    private LinearLayout llSure;
    private TextView tv;
    private EditText etPassword;
    private ImageView ivPwd;
    private ImageView ivIsShowPassword;
    private Button btnRegister;
    private String num;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.forget_fragment, null);
        ivBack = (TextView) view.findViewById(R.id.iv_back);
        etPhoneNum = (EditText) view.findViewById(R.id.et_phone_num);
        tvCode = (TextView) view.findViewById(R.id.tv_code);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        etCode = (EditText) view.findViewById(R.id.et_code);
        llNext = (LinearLayout) view.findViewById(R.id.ll_next);
        llSure = (LinearLayout) view.findViewById(R.id.ll_sure);
        tv = (TextView) view.findViewById(R.id.tv);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        ivPwd = (ImageView) view.findViewById(R.id.iv_pwd);
        ivIsShowPassword = (ImageView) view.findViewById(R.id.iv_isShow_password);
        btnRegister = (Button) view.findViewById(R.id.btn_register);


        return view;
    }

    boolean flag = false;

    @Override
    public void initData() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pwd = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)){
                    Utils.showToast(mActivity,"请设置密码");
//                    Toast.makeText(mActivity,"请设置密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestQueue queues = MyApplication.queues;
                String url= DataInterface.SERVER_UPLOAD+"userAction_getPassword.action?phone="+num+"&password="+pwd;
                StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject json=new JSONObject(s);
                            if (json.getInt("code")==0){
                                SharedPreferences sp=mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("password",pwd);
                                edit.commit();
                                getFragmentManager().popBackStack();
                            }else {
                                Toast.makeText(mActivity,json.getString("errorMessage"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utils.showToast(mActivity,"找回密码失败");
//                            Toast.makeText(mActivity,"找回密码失败",Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Utils.showToast(mActivity,"找回密码失败");
//                        Toast.makeText(mActivity,"找回密码失败",Toast.LENGTH_SHORT).show();
                    }
                });

                queues.add(request);
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


        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * @param v     是否获取焦点，改变右侧小图标的明暗，就是两张图片切换
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivPwd.setImageResource(R.mipmap.password);
                } else {
                    ivPwd.setImageResource(R.mipmap.password_gray);
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = etPhoneNum.getText().toString().trim();
                if (!(!TextUtils.isEmpty(num) && num.length() == 11)) {
                    Utils.showToast(mActivity,"请输入正确的手机号");
//                    Toast.makeText(mActivity, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Utils.showToast(mActivity,"请输入验证码");
//                    Toast.makeText(mActivity, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86", num, code);
                SMSSDK.registerEventHandler(eh);
            }
        });
        //手机编辑框的监听，当数字长度等于十一的时候，可以获取验证码
        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 11 && tvCode.getText().toString().trim().equals("重新获取")) {
                    tvCode.setBackgroundColor(Color.parseColor("#46b6f9"));
                    tvCode.setTextColor(Color.parseColor("#eeffffff"));
                    tvCode.setFocusable(true);
                } else {
                    tvCode.setBackgroundColor(Color.parseColor("#e5e5e5"));
                    tvCode.setTextColor(Color.parseColor("#cccccc"));
                    tvCode.setFocusable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvCode.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v   点击获取验证码
             */
            @Override
            public void onClick(View v) {
//                SMSSDK.initSDK(mActivity, "155bff5f6ce3a", "bcb8c69b2d68ab969a3aa608dfbc32ed");
                String phoneNum = etPhoneNum.getText().toString().trim();
                tvCode.setFocusable(false);
                SMSSDK.getVerificationCode("86", phoneNum);
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            /**
             * @param v  返回监听
             */
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }
}
