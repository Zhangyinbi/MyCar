package cn.ityun.com.carservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import cn.ityun.com.carservice.R;


/**
 * Created by Administrator on 2016/6/21 0021.
 */
public class RefreshListView extends ListView {
    private View mHeaderView;
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
        this.addHeaderView(mHeaderView);
        mHeaderView.measure(0, 0);
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
                   /* Animation translateAnimation1;
                    translateAnimation1 = new TranslateAnimation(0.0f, 0.0f,z, endY - startY);
                    z=endY - startY;
                    translateAnimation1.setDuration(1);
                    this.startAnimation(translateAnimation1);*/
                    int padding = dy - mHeaderViewheight;
                    mHeaderView.setPadding(0, padding, 0, 0);
                    return true;
                } else {
                    this.setPadding(0, dy, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (endY == -1) {
                    break;
                }
                mHeaderView.setPadding(0, -mHeaderViewheight, 0, 0);
                this.setPadding(0, 0, 0, 0);
                Animation translateAnimation;
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, endY - startY, 0);
                translateAnimation.setDuration(700);
                this.startAnimation(translateAnimation);
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
}
