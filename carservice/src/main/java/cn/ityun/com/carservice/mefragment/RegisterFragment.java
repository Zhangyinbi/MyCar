package cn.ityun.com.carservice.mefragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
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


import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.global.DataInterface;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.Utils;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class RegisterFragment extends BaseFragment {
    private boolean flag = false;
    private ImageView ivIsShowPassword;
    private EditText etPassword;
    private EditText etPhoneNum;
    private ImageView ivBack;
    private TextView tvCode;
    private Handler mHandler;
    private Button btnRegister;
    private String phoneNum;
    private EditText etCode;
    int count=59;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.register_fragment, null);
        ivIsShowPassword = (ImageView)view.findViewById(R.id.iv_isShow_password);
        etPassword = (EditText)view.findViewById(R.id.et_password);
        etPhoneNum = (EditText) view.findViewById(R.id.et_phone_num);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvCode = (TextView) view.findViewById(R.id.tv_code);
        etCode = (EditText) view.findViewById(R.id.et_code);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        mHandler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                if (msg.what==1){
                    tvCode.setText("重新获取");
                    tvCode.setTextColor(Color.parseColor("#77000000"));
                    tvCode.setClickable(true);
                    mHandler.removeMessages(0);
                    String  result = (String) msg.obj;
                    if (result==null){
                        Utils.showToast(mActivity,"获取验证码失败，请重新获取");
//                        Toast.makeText(mActivity,"获取验证码失败，请重新获取",Toast.LENGTH_SHORT).show();
                        count=59;
                        return;
                    }
//                    Log.e("========", result);
                    Toast.makeText(mActivity,result,Toast.LENGTH_SHORT).show();
                    count=59;
                    return;
                }
                if (msg.what==2){
                    Utils.showToast(mActivity,"验证码已发送至您手机，请注意查收");
//                    Toast.makeText(mActivity,"验证码已发送至您手机，请注意查收",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msg.what==3){
                    //注册成功之后的操作，把账号密码上传至服务器，并在本地保存一份，切设置一个状态用来
                    //记录当前是否是登录状态，在记录一个开始时间，以后每次登陆也记录一个时间，当时间间隔相差15天时
                    //让用户重新登陆
                    String url= DataInterface.SERVER_UPLOAD+"userAction_addUser.action?phone="+etPhoneNum.getText().toString().trim()+"&password="+etPassword.getText().toString().trim();
                    RequestQueue queues = MyApplication.queues;
                    StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        /**
                         * @param s
                         */
                        @Override
                        public void onResponse(String s) {
                            Log.e("------------", s );
                            try {
                                JSONObject json=new JSONObject(s);
                                int  code = Integer.parseInt(json.getString("code"));
                                if (code==0){
                                    JSONObject data = json.getJSONObject("data");
                                    SharedPreferences sp=mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putString("username",etPhoneNum.getText().toString().trim());
                                    edit.putString("password",etPassword.getText().toString().trim());
                                    edit.putInt("id",data.getInt("id"));
                                    edit.putString("nickname",data.getString("userName"));
                                    edit.putString("headPic","default.jpg");
                                    edit.commit();
                                    getFragmentManager().popBackStack();
                                }else {
                                    if (json.getString("errorMessage").equals("电话已存在！")){
                                        getFragmentManager().popBackStack();
                                    }else {
                                        Toast.makeText(mActivity,json.getString("errorMessage"),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast(mActivity,"注册失败");
//                                Toast.makeText(mActivity,"注册失败",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Utils.showToast(mActivity,"注册失败");
//                            Toast.makeText(mActivity,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                    queues.add(request);

                }

                tvCode.setText(count--+"s");
                tvCode.setTextColor(Color.parseColor("#0099ff"));
                mHandler.sendEmptyMessageDelayed(0, 1000);
                if (count==0){
                    tvCode.setText("重新获取");
                    tvCode.setTextColor(Color.parseColor("#77000000"));
                    mHandler.removeMessages(0);
                    count=59;
                    tvCode.setClickable(true);
                }
            }
        };






        return view;
    }

    EventHandler eh= new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    //去做注册操作
                    mHandler.sendEmptyMessage(3);
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    //弹出toast 提示用户查看手机
                    mHandler.sendEmptyMessage(2);
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                }
            }else{
                ((Throwable)data).printStackTrace();
//                Log.e("=============", ((Throwable) data).getMessage() );
                Message message = Message.obtain();
                String resu=((Throwable) data).getMessage();
                try {
                    JSONObject json=new JSONObject(resu);
                    message.obj=json.getString("detail");
                    message.what=1;
//                    Log.e("===========json==", json.getString("detail") );
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(1);
                }

            }
        }
    };

    @Override
    public void initData() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = etPhoneNum.getText().toString().trim();
                if (phoneNum!=null&&phoneNum.length()==11){
//                    SMSSDK.initSDK(mActivity, "155bff5f6ce3a", "bcb8c69b2d68ab969a3aa608dfbc32ed");
                }else {
                    Utils.showToast(mActivity,"请输入正确的手机号码");
//                    Toast.makeText(mActivity,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String trim = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(trim)){
                    Utils.showToast(mActivity,"请输入验证码");
//                    Toast.makeText(mActivity,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwd = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)){
                    Utils.showToast(mActivity,"请输入密码");
//                    Toast.makeText(mActivity,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode("86",phoneNum,trim);
//                Log.e("-----------", "phoneNum: "+phoneNum+",trim" +trim);
                SMSSDK.registerEventHandler(eh);
            }
        });




        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = etPhoneNum.getText().toString().trim();
                if (count==59){
                    tvCode.setClickable(false);
                }
                if (phoneNum.length()==11){
//                    SMSSDK.initSDK(mActivity, "155bff5f6ce3a", "bcb8c69b2d68ab969a3aa608dfbc32ed");
                    SMSSDK.getVerificationCode("86", phoneNum);
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }else {
                    Utils.showToast(mActivity,"请输入正确的手机号码");
//                    Toast.makeText(mActivity,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                    tvCode.setClickable(true);
                }


            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
        }
        });
        ivIsShowPassword.setOnClickListener(new View.OnClickListener() {
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







    }
}
