package cn.ityun.com.carservice.homefragment.violation;

import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ityun.com.carservice.MainActivity;
import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.fragment.BaseFragment;
import cn.ityun.com.carservice.utils.MyDbOpenHelper;
import cn.ityun.com.carservice.view.sortlistview.CharacterParser;
import cn.ityun.com.carservice.view.sortlistview.PinyinComparator;
import cn.ityun.com.carservice.view.sortlistview.SideBar;
import cn.ityun.com.carservice.view.sortlistview.SortAdapter;
import cn.ityun.com.carservice.view.sortlistview.SortModel;

/**
 * Created by Administrator on 2016/7/21 0021.
 */
public class CarTypeInfoFragment extends BaseFragment {
    private ImageButton ivBack;
    //SortModel  是json数据
    private List<SortModel> SourceDateList;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private SideBar sideBar;
    private TextView dialog;
    private ListView sortListView;
    private SortAdapter adapter;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.car_type_info_fragment, null);
        final MainActivity mainUI = (MainActivity) mActivity;
        ivBack = (ImageButton) view.findViewById(R.id.ib_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                mainUI.car=SourceDateList.get(position).getName();
                getFragmentManager().popBackStack();
            }
        });
        ArrayList<SortModel> list=new ArrayList<SortModel>();
        Cursor cursor = MyDbOpenHelper.dbHelper.rawQuery("select * from info", null);
        while (cursor.moveToNext()){
            SortModel data= new SortModel();
            data.name=cursor.getString(cursor.getColumnIndex("carName"));
            list.add(data);
        }
        //数据源，这个是为了获取SortModel的sortLetters的属性值，其实没有多大必要，接口返回数据中有拼音，所以不需要把汉字转化为拼音
        // 很有必要因为不是单纯的拼音，使用了CharacterParser来得到可排序的数据源  只有这样才可以调用Collection.sort方法
        SourceDateList = filledData(list);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(mActivity,SourceDateList);
        sortListView.setAdapter(adapter);
        return mainUI.show(view, Color.parseColor("#0f97ee"));
    }



    /**
     * 为ListView填充数据
     *
     * @param list 为了拿到首字母   完全没必要，接口返回的数据有simpleName，可以直接截取首字母并大写
     *             很有必要  需要CharacterParser  用的是ASCII码来进行的
     * @return
     */
    private List<SortModel> filledData(ArrayList<SortModel> list) {
        List<SortModel> mSortList = new ArrayList<SortModel>();
        for (int i = 0; i < list.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.name = list.get(i).name;
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(list.get(i).name);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

}
