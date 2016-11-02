package cn.ityun.com.carservice.mefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

import org.xmlpull.v1.XmlPullParser;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import cn.ityun.com.carservice.AlbumActivity;
import cn.ityun.com.carservice.GalleryActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.global.DataInterface;
import cn.ityun.com.carservice.util.Bimp;
import cn.ityun.com.carservice.util.FileUtils;
import cn.ityun.com.carservice.util.ImageItem;
import cn.ityun.com.carservice.util.PublicWay;
import cn.ityun.com.carservice.util.Res;
import cn.ityun.com.carservice.utils.Utils;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class PostFragment extends cn.ityun.com.carservice.fragment.BaseFragment {
    private PopupWindow pop = null;
    private TextView tvCancel;
    private LinearLayout ll_popup;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private View vieww;
    public static Bitmap bimap ;
    private ImageItem takePhoto=null;
    private TextView tvSend;
    private EditText etContent;
    private SharedPreferences sp;

    @Override
    public View initView() {
        vieww = View.inflate(mActivity, R.layout.post_your_idea_fragment,null);
        Res.init(mActivity);
        bimap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_addpic_unfocused);
        PublicWay.activityList.add(mActivity);
        tvCancel = (TextView) vieww.findViewById(R.id.tv_cancel);
        tvSend = (TextView) vieww.findViewById(R.id.tv_send);
        etContent = (EditText) vieww.findViewById(R.id.et_content);
        noScrollgridview = (GridView) vieww.findViewById(R.id.noScrollgridview);
        Init();
        return mainUi.show(vieww, Color.parseColor("#0f97ee"));
    }

    private void Init() {
        pop = new PopupWindow(mActivity);

        View view =mActivity.getLayoutInflater().inflate(R.layout.item_popupwindows, null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mActivity,AlbumActivity.class);
                startActivityForResult(intent,0);
                mActivity.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });


        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(mActivity);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(mActivity,R.anim.activity_translate_in));
                    pop.showAtLocation(vieww, Gravity.BOTTOM, 0, 0);
                } else {
                    Intent intent = new Intent(mActivity, GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });

    }
    private static final int TAKE_PICTURE = 0x000001;
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode ==mActivity.RESULT_OK) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);
                    takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }
    @Override
    public void initData() {
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())||Bimp.tempSelectBitmap.size()!=0){
                    AsyncHttpClient client = new AsyncHttpClient();
                    String url = DataInterface.SERVER_UPLOAD + "notes/notesAction_addNote.action";
                    sp = mActivity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    RequestParams params = new RequestParams(url);
                    params.addBodyParameter("userId",sp.getInt("id",-1)+"");
                    if (TextUtils.isEmpty(etContent.getText().toString().trim())){
                        params.addBodyParameter("content","");
                    }else {
                        params.addBodyParameter("content",etContent.getText().toString().trim());
                    }
                    ArrayList<ImageItem> list = Bimp.tempSelectBitmap;
                    if (list.size()>0){
                        for (ImageItem item:list){

                            File file = new File(item.getImagePath());
                            params.addBodyParameter("img",file);
                        }
                    }
                    x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.e("------------", "onSuccess:"+result );
                            Utils.showToast(mActivity,"发表成功");
                            Bimp.tempSelectBitmap.clear();
                            Bimp.max = 0;
                            getFragmentManager().popBackStack();
                        }
                        @Override
                        public void onError(Throwable throwable, boolean b) {
//                            Log.e("--------------", "throwable: "+throwable.toString() );
                        }
                        @Override
                        public void onCancelled(CancelledException e) {
//                            Log.e("--------------", "onCancelled: " );
                        }
                        @Override
                        public void onFinished() {
//                            Log.e("--------------", "onFinished: " );
                        }
                    });
                }
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bimp.tempSelectBitmap.clear();
                Bimp.tempSelectBitmap.clear();
                Bimp.max = 0;
                getFragmentManager().popBackStack();
            }
        });
    }
    public class GridAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if(Bimp.tempSelectBitmap.size() == 9){
                return 9;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position ==Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    @Override
    public void onResume() {
        adapter.update();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
        Bimp.max = 0;
    }

}
