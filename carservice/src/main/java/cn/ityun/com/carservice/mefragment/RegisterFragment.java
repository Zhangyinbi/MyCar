package cn.ityun.com.carservice.mefragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ityun.com.carservice.R;

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

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.register_fragment, null);
        ivIsShowPassword = (ImageView)view.findViewById(R.id.iv_isShow_password);
        etPassword = (EditText)view.findViewById(R.id.et_password);
        etPhoneNum = (EditText) view.findViewById(R.id.et_phone_num);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        tvCode = (TextView) view.findViewById(R.id.tv_code);




        return view;
    }
int count=59;
    @Override
    public void initData() {
        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = etPhoneNum.getText().toString().trim();
                if (phoneNum.length()==11){
                    tvCode.setClickable(false);

                    mHandler = new Handler() {
                        @Override
                        public void handleMessage( Message msg) {
                            tvCode.setText(count--+"s");
                            tvCode.setTextColor(Color.parseColor("#0099ff"));
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                            if (count==0){
                                tvCode.setText("获取严重码");
                                tvCode.setTextColor(Color.parseColor("#77000000"));
                                mHandler.removeMessages(0);
                                count=59;
                                tvCode.setClickable(true);
                            }
                        }
                    };
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }else {
                    Toast.makeText(mActivity,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
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
