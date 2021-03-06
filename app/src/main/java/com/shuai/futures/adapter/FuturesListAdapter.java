package com.shuai.futures.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.data.FuturesInfo;
import com.shuai.futures.data.FuturesPrice;
import com.shuai.futures.ui.CandleStickActivity;

import java.util.List;

/**
 *
 */
public class FuturesListAdapter extends ArrayAdapter<FuturesPrice> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<FuturesInfo> mFuturesList;

    public FuturesListAdapter(@NonNull Context context,
                              @NonNull List<FuturesInfo> futuresList,
                              @NonNull List<FuturesPrice> objects) {
        super(context, 0, objects);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFuturesList = futuresList;
    }

    private static class ViewHolder {
        TextView mTvName;
        TextView mTvId;
        TextView mTvCurrentPrice;
        TextView mTvPercent;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final FuturesPrice item = getItem(position);
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_futures_info, parent, false);
            holder = new ViewHolder();
            holder.mTvName = (TextView) view.findViewById(R.id.tv_name);
            holder.mTvId = (TextView) view.findViewById(R.id.tv_id);
            holder.mTvCurrentPrice = (TextView) view.findViewById(R.id.tv_price);
            holder.mTvPercent = (TextView) view.findViewById(R.id.tv_percent);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvName.setText(item.mTitle);
        holder.mTvId.setText(item.mName);
        holder.mTvCurrentPrice.setText(String.format("%.2f", item.mCurrentPrice));

        double percent = item.getPercent();
        if (percent >= 0) {
            holder.mTvPercent.setBackgroundResource(R.color.up);
            holder.mTvPercent.setText(String.format("+%.2f%%", percent * 100));
        } else {
            holder.mTvPercent.setBackgroundResource(R.color.down);
            holder.mTvPercent.setText(String.format("%.2f%%", percent * 100));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CandleStickActivity.class);
                intent.putExtra(Constants.EXTRA_FUTURES_ID, getFuturesId(item.mName));
                intent.putExtra(Constants.EXTRA_FUTURES_NAME, item.mName);
                intent.putExtra(Constants.EXTRA_FUTURES_TITLE, item.mTitle);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    private String getFuturesId(String futuresName) {
        //将新浪财经的id转为自己服务端id
        String futuresId = null;
        for (FuturesInfo item : mFuturesList) {
            if (item.mName.equalsIgnoreCase(futuresName)) {
                futuresId = item.mId;
                break;
            }
        }
        return futuresId;
    }
}
