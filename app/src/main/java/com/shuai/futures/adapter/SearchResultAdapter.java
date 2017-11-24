package com.shuai.futures.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shuai.futures.R;
import com.shuai.futures.data.DataManager;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.utils.NavigateUtils;

import java.util.List;

/**
 *
 */
public class SearchResultAdapter extends ArrayAdapter<FuturesInfo> {
    private Context mContext;
    private LayoutInflater mInflater;
    private DataManager mDataManager;
    private AddFollowListener mAddFollowListener;

    public interface AddFollowListener {
        void onAddFollow(FuturesInfo item);
    }

    public SearchResultAdapter(Context context, @NonNull List<FuturesInfo> futuresList) {
        super(context, 0, futuresList);
        mContext = context;
        mDataManager = DataManager.getInstance();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAddFollowListener(AddFollowListener listener) {
        mAddFollowListener = listener;
    }

    private static class ViewHolder {
        TextView mTvTitle;
        TextView mTvName;
        ImageButton mBtnAdd;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FuturesInfo item = getItem(position);
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_search_futures_result, parent, false);
            holder = new ViewHolder();
            holder.mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.mTvName = (TextView) view.findViewById(R.id.tv_name);
            holder.mBtnAdd = (ImageButton) view.findViewById(R.id.ib_add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateUtils.showCandleStickActivity(mContext, item);
            }
        });

        holder.mTvTitle.setText(item.mTitle);
        holder.mTvName.setText(item.mName);
        if (mDataManager.isFollowedFutures(item.mId)) {
            holder.mBtnAdd.setBackgroundResource(R.drawable.btn_added);
            holder.mBtnAdd.setOnClickListener(null);
        } else {
            holder.mBtnAdd.setBackgroundResource(R.drawable.btn_add);
            holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddFollowListener != null) {
                        mAddFollowListener.onAddFollow(item);
                    }
                }
            });
        }
        return view;
    }

}
