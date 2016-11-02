package cn.ityun.com.carservice.homefragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.bean.ViolationCarInfo;
import cn.ityun.com.carservice.fragment.BaseFragment;
import cn.ityun.com.carservice.homefragment.violation.AddCarInfoFragment;
import cn.ityun.com.carservice.homefragment.violation.IllegalInfoListFragment;
import cn.ityun.com.carservice.utils.MyDbOpenHelper;
import cn.ityun.com.carservice.utils.Utils;
import cn.ityun.com.carservice.view.swipemenu.SwipeMenu;
import cn.ityun.com.carservice.view.swipemenu.SwipeMenuCreator;
import cn.ityun.com.carservice.view.swipemenu.SwipeMenuItem;
import cn.ityun.com.carservice.view.swipemenu.SwipeMenuListView;

/**
 * Created by Administrator on 2016/7/20 0020.
 */
public class ViolationCarListFragment extends BaseFragment {
    private ArrayList<ViolationCarInfo> list;
    private ImageButton ivBack;
    private SwipeMenuListView lvList;
    private ImageButton ibAddCarInfo;
    private LinearLayout layout;
    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ViolationCarInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder myHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.violation_car_list_item, null);
                myHolder = new ViewHolder();
                myHolder.rlLayout = (RelativeLayout) convertView.findViewById(R.id.rl_item);
                myHolder.tvMsg = (TextView) convertView.findViewById(R.id.tv_msg);
                myHolder.tvCarNum = (TextView) convertView.findViewById(R.id.tv_car_num);
                myHolder.tvCarType = (TextView) convertView.findViewById(R.id.tv_car_type);
                myHolder.tvViolationNum = (TextView) convertView.findViewById(R.id.tv_violation_num);
                myHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                myHolder.tvDeductingScore = (TextView) convertView.findViewById(R.id.tv_deducting_score);
                convertView.setTag(myHolder);
            }
            {
                myHolder = (ViewHolder) convertView.getTag();
            }
            myHolder.tvCarNum.setText(list.get(position).tvCarNum);
            myHolder.rlLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag = Utils.netstatus(mainUi);
                    if (!flag) {
                        IllegalInfoListFragment fragment = new IllegalInfoListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("lsprefix", list.get(position).lsprefix);
                        bundle.putString("lsnum", list.get(position).lsnum);
                        bundle.putString("lstype", list.get(position).lstype);
                        bundle.putString("frameno", list.get(position).frameno);
                        bundle.putString("carorg", list.get(position).carorg);
                        bundle.putString("type", list.get(position).tvCarType);

//                        Log.e("-------------", "onClick: "+ str+"--"+String.valueOf(etCarNum.getText())
//                        +"----"+code+"---"+String.valueOf(etCarCode.getText())+"----"+carorg);
                        fragment.setArguments(bundle);
                        //携带参数数据
                        jumpFragment(fragment, true);
                    }

//                        Toast.makeText(mActivity,"还没有写好",Toast.LENGTH_LONG).show();
                }
            });
            myHolder.tvCarType.setText(list.get(position).tvCarType);
            myHolder.tvViolationNum.setText(list.get(position).count);
            myHolder.tvPrice.setText(list.get(position).price);
            myHolder.tvDeductingScore.setText(list.get(position).score);
            myHolder.tvMsg.setText(list.get(position).msg);
            return convertView;
        }
    };

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.violation_fragment, null);
        MainActivity mainUI = (MainActivity) mActivity;
        ivBack = (ImageButton) view.findViewById(R.id.ib_back);
        ibAddCarInfo = (ImageButton) view.findViewById(R.id.ib_add_car);
        lvList = (SwipeMenuListView) view.findViewById(R.id.lv_list);
        layout = (LinearLayout) view.findViewById(R.id.ll_no_car);


        ibAddCarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new 出来一个新的fragment
                 /*
                  * 这个地方往查询结果界面进行跳转
                  *需要携带参数,注意携带参数的类型和种类,使用setArguement()
                  */
                jumpFragment(new AddCarInfoFragment(), true);
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return mainUI.show(view, Color.parseColor("#0f97ee"));
    }

    @Override
    public void initData() {
//        Log.e("===========", "initData:woyou进来了 " );

        Cursor cursor = MyDbOpenHelper.dbHelper.rawQuery("select * from carinfo", null);
        if (cursor.getCount() == 0) {
            layout.setVisibility(View.VISIBLE);
            lvList.setVisibility(View.GONE);
            return;
        }
        cursor = MyDbOpenHelper.dbHelper.query("carinfo", null, null, null, null, null, "time desc");
        list = new ArrayList<ViolationCarInfo>();
        while (cursor.moveToNext()) {
            ViolationCarInfo info = new ViolationCarInfo();
            info.carorg = cursor.getString(cursor.getColumnIndex("carorg"));
            info.frameno = cursor.getString(cursor.getColumnIndex("frameno"));
            info.lsnum = cursor.getString(cursor.getColumnIndex("lsnum"));
            info.lsprefix = cursor.getString(cursor.getColumnIndex("lsprefix"));
            info.lstype = cursor.getString(cursor.getColumnIndex("lstype"));
            info.count = cursor.getString(cursor.getColumnIndex("count"));
            info.price = cursor.getString(cursor.getColumnIndex("price"));
            info.score = cursor.getString(cursor.getColumnIndex("score"));
            info.tvCarType = cursor.getString(cursor.getColumnIndex("type"));
            info.tvCarNum = info.lsprefix + info.lsnum;
            info.msg = cursor.getString(cursor.getColumnIndex("msg"));
            list.add(info);
        }
        layout.setVisibility(View.GONE);
        lvList.setVisibility(View.VISIBLE);
        lvList.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                  /*  // create "open" item
                    SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity.getApplicationContext());
                    // set item background
//                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    // set item width
                    deleteItem.setWidth(dp2px(mActivity.getApplicationContext(), 80));
                    // set item title
                    deleteItem.setTitle("删除");
                    // set item title fontsize
                    deleteItem.setTitleSize(16);
                    // set item title font color
                    deleteItem.setTitleColor(mActivity.getResources().getColor(R.color.write));
                    // add to menu
                    menu.addMenuItem(deleteItem);*/

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(mActivity.getApplicationContext());
                // set item background
//                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                            0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Utils.dp2px(mActivity.getApplicationContext(), 90));
                // set a icon
                deleteItem.setIcon(R.drawable.delete);
                deleteItem.setBackground(R.drawable.menu_color_selector);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        lvList.setMenuCreator(creator);
        lvList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        MyDbOpenHelper.dbHelper.delete("carinfo", "chepai=?", new String[]{list.get(position).tvCarNum});
                        list.remove(position);
                        if (list.size() == 0) {
                            layout.setVisibility(View.VISIBLE);
                            lvList.setVisibility(View.GONE);
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });


    }

    private class ViewHolder {
        private RelativeLayout rlLayout;
        private TextView tvCarNum;
        private TextView tvCarType;
        private TextView tvViolationNum;
        private TextView tvPrice;
        private TextView tvMsg;
        private TextView tvDeductingScore;
    }


    /**
     * 这个方法中可以写一个回调函数，把数据传给上一个fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
