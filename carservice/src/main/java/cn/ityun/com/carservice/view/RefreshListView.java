package cn.ityun.com.carservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.ityun.com.carservice.R;


/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class RefreshListView extends ListView implements AdapterView.OnItemClickListener {
    private View mHeaderView;
    private View mFooterView;
    int mHeaderViewheight;
    int startY = -1;
    int endY = -1;
    private Context context;

    public RefreshListView(Context context) {
        super(context);
        this.context = context;
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initHeaderView();
    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
//        mFooterView=View.inflate(getContext(),R.layout.ll,null);
        this.addHeaderView(mHeaderView);
//        this.addFooterView(mFooterView);
        mHeaderView.measure(0, 0);
//        mFooterView.measure(0, 0);
//        mFooterView.setPadding(0, -mFooterView.getMeasuredHeight(), 0, 0);
        mHeaderViewheight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewheight, 0, 0);//隐藏头布局



    }

    int z = 0;
    boolean flag = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (flag) {
                    startY = (int) ev.getRawY();
                    flag = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getRawY();
                }
                endY = (int) ev.getRawY();
                int dy = endY - startY;

                if (dy > 0) {
                    int padding = dy - mHeaderViewheight;
                    mHeaderView.setPadding(0, padding, 0, 0);
//                    return true;
                } else {
//                    Log.e("-------", "onTouchEvent:"+dy);
                    this.setPadding(0, dy, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (endY == -1) {
                    break;
                }
                mHeaderView.setPadding(0, -mHeaderViewheight, 0, 0);
                this.setPadding(0, 0, 0, 0);
                int pad=endY - startY;
                if (this.getFirstVisiblePosition()==0&&pad>0){
                    Animation translateAnimation;
                    translateAnimation = new TranslateAnimation(0.0f,0.0f,pad , 0);
                    translateAnimation.setDuration(700);
                    this.startAnimation(translateAnimation);
                }
                if (this.getLastVisiblePosition()==this.getCount()-1&&pad<0){
                    if (isLastItemVisible()){
                        Animation translateAnimation;
                        translateAnimation = new TranslateAnimation(0.0f,0.0f,pad,0);
                        translateAnimation.setDuration(700);
                        this.startAnimation(translateAnimation);
                    }
                }
                startY = -1;
                endY = -1;
                z = 0;
                flag = true;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


    protected boolean isLastItemVisible() {
        final Adapter adapter = getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = getLastVisiblePosition();


        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - getFirstVisiblePosition();
            final int childCount = getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= getBottom();
            }
        }

        return false;
    }

    OnItemClickListener mItemClickListener;

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(this);
        mItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }

    }
}
