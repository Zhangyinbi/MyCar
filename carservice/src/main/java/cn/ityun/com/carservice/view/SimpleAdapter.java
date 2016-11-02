package cn.ityun.com.carservice.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ityun.com.carservice.R;
import cn.ityun.com.carservice.bean.Sheng;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class SimpleAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    public List<Sheng> datas;



    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public SimpleAdapter(Context context, List<Sheng> datas) {
        this.context = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_single_textview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(datas.get(position).lsprefix);
        setUpItemEvent(holder);
    }

    public void setUpItemEvent(final MyViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

        }



       /* holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,""+position,Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tv;

    public MyViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.id_tv);
    }
}