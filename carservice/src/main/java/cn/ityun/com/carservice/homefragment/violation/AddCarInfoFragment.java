package cn.ityun.com.carservice.homefragment.violation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.bean.City;
import cn.ityun.com.carservice.bean.Sheng;
import cn.ityun.com.carservice.bean.Size;
import cn.ityun.com.carservice.fragment.BaseFragment;
import cn.ityun.com.carservice.utils.MyDbOpenHelper;
import cn.ityun.com.carservice.utils.Utils;
import cn.ityun.com.carservice.view.RefreshListView;
import cn.ityun.com.carservice.view.SimpleAdapter;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class AddCarInfoFragment extends BaseFragment {
    private RefreshListView lvList;
    private RelativeLayout rlCarType;
    private TextView tvCarType;
    private ArrayList<Size> sizes;
    private TextView tv;
    private EditText etCarNum;
    private EditText etCarCode;
    private Animation anim;
    private String str = "豫";
    private String str1 = "郑州";
    private String carorg="zhengzhou";
    private SimpleAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageButton ivBack;
    private RelativeLayout rlCar;
    private TextView tvCarSize;
    private ImageView ivSizeQuestion;
    private AlertDialog alertDialog;
    private ImageView ivCodeQuestion;
    private TextView tvProvince;
    private ImageView ibCheck;
    private ArrayList<Sheng> list;
    private ArrayList<City> citys;

    View.OnClickListener myClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRecyclerView.isShown()) {
                hindView();
                return;
            }

            ibCheck.setImageResource(R.mipmap.blue_top);
            mRecyclerView.setVisibility(View.VISIBLE);

            LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(mainUi, R.anim.recycler_zoom_in));
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            mRecyclerView.setLayoutAnimation(lac);
            mRecyclerView.startLayoutAnimation();
            mRecyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv.setVisibility(View.VISIBLE);
                    anim = AnimationUtils.loadAnimation(mActivity, R.anim.recycler_zoom_in);
                    tv.startAnimation(anim);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


//           showPopupWindowProvince(v);
        }
    };
    private Button btnSearch;
    private TextView tvCity;
    private SQLiteDatabase dbHelper;
    private ListView listView;


    private String province = "河南";
    private TextView tvBack;

    @Override
    public void initData() {
        SQLiteDatabase dbHelper = MyDbOpenHelper.dbHelper;
        Cursor cursor1 = dbHelper.rawQuery("select * from size", null);
        sizes = new ArrayList<>();
        while (cursor1.moveToNext()) {
            Size size = new Size();
            size.name = cursor1.getString(cursor1.getColumnIndex("name"));
            size.code = cursor1.getString(cursor1.getColumnIndex("code"));
            sizes.add(size);
        }
        list = new ArrayList<Sheng>();
        dbHelper = MyDbOpenHelper.dbHelper;
        Cursor cursor = dbHelper.rawQuery("select * from prov", null);
        while (cursor.moveToNext()) {
            Sheng sheng = new Sheng();
            sheng.carorg = cursor.getString(cursor.getColumnIndex("carorg"));
            sheng.lsprefix = cursor.getString(cursor.getColumnIndex("lsprefix"));
            sheng.province = cursor.getString(cursor.getColumnIndex("province"));
            list.add(sheng);
        }
        mAdapter = new SimpleAdapter(mActivity, list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 9));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setmOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(mActivity,"Click--"+position,Toast.LENGTH_LONG).show();
                tvProvince.setText(list.get(position).lsprefix);
                str = list.get(position).lsprefix;
                hindView();
                province = list.get(position).province;
                str1 = "";
                carorg="";
                tvCity.setText(str1);


            }

        });


    }

    /*private void showPopupWindowProvince(View view) {
        final View contentView = View.inflate(mActivity,R.layout.province_list, null);
        lvList = (RefreshListView) contentView.findViewById(R.id.lv_list);
         final PopupWindow popupWindow= new PopupWindow(contentView, 140, 400, true);;
        lvList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View vie =View.inflate(mActivity,R.layout.province_list_item,null);
                final TextView tvPrivince= (TextView) vie.findViewById(R.id.tv_province);
                tvPrivince.setText(list.get(position).lsprefix);
                tvPrivince.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvProvince.setText(tvPrivince.getText());
                        popupWindow.dismiss();
                    }
                });
                return vie;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.setAlpha(one.0f,mainUi);
                ibCheck.setImageResource(R.mipmap.blue_bottom);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.color_small_box));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view,-45,0);
        ibCheck.setImageResource(R.mipmap.blue_top);

    }*/

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.add_car_info_fragment, null);
//        final MainActivity mainUI = (MainActivity) mActivity;
        tvBack = (TextView) view.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate(null,1);
            }
        });
        tvCity = (TextView) view.findViewById(R.id.tv_city);
        tvCity.setText(str1);
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                citys = new ArrayList<City>();
                SQLiteDatabase dbHelper = MyDbOpenHelper.dbHelper;
                Cursor cursor = dbHelper.rawQuery("select * from " + province, null);
                while (cursor.moveToNext()) {
                    City city = new City();
                    city.carorg = cursor.getString(cursor.getColumnIndex("carorg"));
                    city.city = cursor.getString(cursor.getColumnIndex("city"));
                    city.lsnum = cursor.getString(cursor.getColumnIndex("lsnum"));
                    citys.add(city);
                }
                View popView = LayoutInflater.from(mActivity).inflate(R.layout.pop_city, null);
                listView = (ListView) popView.findViewById(R.id.lv_city_list);
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return citys.size();
                    }

                    @Override
                    public City getItem(int position) {
                        return citys.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = View.inflate(mActivity, R.layout.pop_city_list_item, null);
                        TextView tvCity = (TextView) view.findViewById(R.id.tv_city);
                        tvCity.setText(citys.get(position).city);


                        return view;
                    }
                });


                final PopupWindow pop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, Utils.ScreenHight(mActivity) / 3, true);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                pop.setBackgroundDrawable(dw);
                pop.setFocusable(true);
                pop.setTouchable(true);
                pop.setAnimationStyle(R.style.AnimBottom);
                pop.showAtLocation(mActivity.findViewById(R.id.id_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        str1 = citys.get(position).city;
                        tvCity.setText(str1);
                        carorg=citys.get(position).carorg;
                        pop.dismiss();
                    }
                });
            }
        });
        btnSearch = (Button) view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                if (!TextUtils.isEmpty(tvCity.getText())&&!TextUtils.isEmpty(etCarNum.getText()) && !TextUtils.isEmpty(etCarCode.getText())) {
                    boolean flag = Utils.netstatus(mainUi);
                    if (!flag){
                        IllegalInfoListFragment fragment = new IllegalInfoListFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("lsprefix",str);
                        bundle.putString("lsnum", String.valueOf(etCarNum.getText()));
                        String code = sizes.get(mCurrentItem).code;
                        bundle.putString("lstype",code);
                        bundle.putString("frameno",String.valueOf(etCarCode.getText()));
                        bundle.putString("carorg",carorg);
                        if (!TextUtils.isEmpty(tvCarType.getText())){
                            bundle.putString("type",tvCarType.getText().toString().trim());
                        }else {
                            bundle.putString("type","");
                        }
//                        Log.e("-------------", "onClick: "+ str+"--"+String.valueOf(etCarNum.getText())
//                        +"----"+code+"---"+String.valueOf(etCarCode.getText())+"----"+carorg);
                        fragment.setArguments(bundle);
                        //携带参数数据
                        jumpFragment(fragment, true);
                    }
                    return;
                }
                Toast.makeText(mActivity, "请输入完整信息", Toast.LENGTH_SHORT).show();

            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerView);
        etCarNum = (EditText) view.findViewById(R.id.et_car_num);
        etCarNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
            }
        });
        etCarCode = (EditText) view.findViewById(R.id.et_car_code);
        etCarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
            }
        });

        tv = (TextView) view.findViewById(R.id.id_tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity,"确定",Toast.LENGTH_LONG).show();
                hindView();

            }
        });
        ivBack = (ImageButton) view.findViewById(R.id.ib_back);
        rlCarType = (RelativeLayout) view.findViewById(R.id.rl_car_type);
        tvCarType = (TextView) view.findViewById(R.id.tv_car_type);
        rlCarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                jumpFragment(new CarTypeInfoFragment(), true);
            }
        });

        tvProvince = (TextView) view.findViewById(R.id.tv_province);
        tvProvince.setText(str);
        tvProvince.setOnClickListener(myClick);
        ibCheck = (ImageView) view.findViewById(R.id.ib_check);
//        ibCheck.setOnClickListener(myClick);

        tvCarSize = (TextView) view.findViewById(R.id.tv_car_size);
        ivSizeQuestion = (ImageView) view.findViewById(R.id.iv_size_question);
        ivCodeQuestion = (ImageView) view.findViewById(R.id.iv_code_question);
        ivCodeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                Utils.setAlpha(0.6f, mainUi);
                showPopupWindowCodeQuestion(v);
            }
        });
        ivSizeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                Utils.setAlpha(0.6f, mainUi);
                showPopupWindowSizeQuestion(v);
            }
        });

        rlCar = (RelativeLayout) view.findViewById(R.id.rl_car);
        rlCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                showChooseDialog();
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hindView();
                getFragmentManager().popBackStack();
            }
        });
        return mainUi.show(view, Color.parseColor("#0f97ee"));
    }

    private void showPopupWindowCodeQuestion(View view) {
// 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.car_code_question_pop, null);

        final PopupWindow popupWindow = new PopupWindow(contentView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                checkPointInCtrlBtn(event);




                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.color_small_box));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.setAlpha(1.0f, mainUi);
            }
        });
    }

    private void showPopupWindowSizeQuestion(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.car_size_question_pop, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                checkPointInCtrlBtn(event);



                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.color_small_box));

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Utils.setAlpha(1.0f, mainUi);
            }
        });

    }

    private void checkPointInCtrlBtn(MotionEvent event) {

        int[] cartLocation = new int[2];
        int[] sortLocation = new int[2];
        ivSizeQuestion.getLocationOnScreen(cartLocation);
        ivCodeQuestion.getLocationOnScreen(sortLocation);
        if (event.getRawX()>cartLocation[0]&&event.getRawX()<cartLocation[0]+ivSizeQuestion.getMeasuredWidth()
                &&event.getRawY()>cartLocation[1]&&event.getRawY()<cartLocation[1]+ivSizeQuestion.getMeasuredHeight()){
            ivSizeQuestion.performClick();
        }else if (event.getRawX()>sortLocation[0]&&event.getRawX()<sortLocation[0]+ivCodeQuestion.getMeasuredWidth()
                &&event.getRawY()>sortLocation[1]&&event.getRawY()<sortLocation[1]+ivCodeQuestion.getMeasuredHeight())
        {
            ivCodeQuestion.performClick();
        }
    }

    private int mCurrentChooseItem;//记录当前选择的item，点击确定之前
    private int mCurrentItem = 0;//确定已经选择的item

    private void showChooseDialog() {
       /*
        自定义dialog  可以自定义布局
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        View v=View.inflate(mActivity,R.layout.car_size_question_pop,null);
        window.setContentView(v);
        v.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup)v.findViewById(R.id.rg)).
            }
        });*/
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        final String[] items = new String[sizes.size()];
        for (int i = 0; i < sizes.size(); i++) {
            items[i] = sizes.get(i).name;
        }
        builder.setTitle("选择车辆型号");
        builder.setIcon(R.mipmap.logo);
        builder.setCancelable(true);

        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvCarSize.setText(items[mCurrentChooseItem]);
                mCurrentItem = mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        tvCarType.setText(mainUi.car);
        mainUi.car = "";
//        Log.e("----------", "onResume: " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tvCarType.setText("");
//        Log.e("---------------", "onDestroyView: " );
    }





    private void hindView() {
        if (mRecyclerView.isShown()) {
            Animation outAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.out_anim);
            mRecyclerView.setAnimation(outAnimation);
            mRecyclerView.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            tv.setAnimation(outAnimation);
            ibCheck.setImageResource(R.mipmap.blue_bottom);
        }
    }
}
