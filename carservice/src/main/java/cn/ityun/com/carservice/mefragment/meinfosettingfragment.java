package cn.ityun.com.carservice.mefragment;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.global.DataInterface;
import cn.ityun.com.carservice.utils.MyApplication;
import cn.ityun.com.carservice.utils.Utils;
import cn.ityun.com.carservice.view.CircleImageView;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class MeInfoSettingFragment extends cn.ityun.com.carservice.fragment.BaseFragment {

    private ImageView ivBack;
    private CircleImageView ivHeadPic;
    private RelativeLayout rlHeadPicSetting;
    private Uri uri;
    private SharedPreferences sp;
    private TextView tvNickname;
    private TextView tvPhoneNum;
    private TextView tvUnLand;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.meinfosetting_fragment, null);
        MainActivity mainUI = (MainActivity) mActivity;
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivHeadPic = (CircleImageView) view.findViewById(R.id.iv_head_pic);
        rlHeadPicSetting = (RelativeLayout) view.findViewById(R.id.rl_head_pic_setting);
        sp = mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ivHeadPic.setImageUrl(DataInterface.SERVER_UPLOAD + "upload/" + sp.getString("headPic", "ooo"), R.mipmap.head_pic);
        tvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        tvPhoneNum = (TextView) view.findViewById(R.id.tv_phone_num);
        tvUnLand = (TextView) view.findViewById(R.id.tv_unLand);


        return mainUI.show(view, Color.parseColor("#0f97ee"));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == mActivity.RESULT_OK) {
                uri = data.getData();
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(data.getData(), "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, data.getData());
                startActivityForResult(intent, 2);
            }
        }
        if (requestCode==2){
            if (resultCode == mActivity.RESULT_OK) {
                //上传头像，然后再把sp文件中的headPic对应的值改掉
                ivHeadPic.setImageURI(uri);
                String realFilePath = Utils.getRealFilePath(mActivity, uri);
                File file = new File(realFilePath);
                String url = DataInterface.SERVER_UPLOAD + "userAction_upLoadPhoto.action";
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("id", sp.getInt("id", -1) + "");
                try {
                    params.put("headPhoto", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                client.post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, String s) {
                        super.onSuccess(i, s);
                        SharedPreferences.Editor edit = sp.edit();
                        try {
                            JSONObject json = new JSONObject(s);
                            edit.putString("headPic", json.getString("data"));
                            edit.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable, String s) {
                        super.onFailure(throwable, s);
                    }
                });
            }
        }
    }

    @Override
    public void initData() {
        tvUnLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = LayoutInflater.from(mActivity).inflate(R.layout.sure_or_cancle, null);
                final PopupWindow popupWindow = new PopupWindow(contentView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
                TextView tv_quit = (TextView) contentView.findViewById(R.id.tv_quit);
                tv_quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("flag", false);
                        edit.commit();
                        popupWindow.dismiss();
                        getFragmentManager().popBackStack();
                    }
                });
                TextView tv_cancel = (TextView) contentView.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                ColorDrawable dw = new ColorDrawable(0000000000);
                popupWindow.setBackgroundDrawable(dw);
                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setAnimationStyle(R.style.AnimBottom);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
//                        Log.i("mengdd", "onTouch : ");
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                // 我觉得这里是API的一个bug
                Utils.setAlpha(0.8f, mActivity);
                // 设置好参数之后再show
                popupWindow.showAtLocation(mActivity.findViewById(R.id.ll_ll), Gravity.BOTTOM, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Utils.setAlpha(1.0f, mActivity);
                    }
                });
            }
        });
        rlHeadPicSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        tvPhoneNum.setText(sp.getString("username", ""));
        tvNickname.setText(sp.getString("nickname", ""));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }
}
