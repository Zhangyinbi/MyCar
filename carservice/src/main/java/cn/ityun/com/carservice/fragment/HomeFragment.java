package cn.ityun.com.carservice.fragment;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.home.CarListFragment;

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
    private MainActivity mainUi;

    @Override
    public View initView() {
        mainUi = (MainActivity) mActivity;
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
            ft.replace(mainUi.flLayout.getId(), new CarListFragment());
        }
        if (i == 2) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }
        if (i == 3) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
        }
        if (i == 4) {
            ft.replace(mainUi.flLayout.getId(), new FindFragment());
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
        mainUi.rGroup.setVisibility(View.VISIBLE);
    }
}
