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
import com.shuai.futures.data.FuturesInfo;

import java.util.List;

/**
 *
 */
public class SearchAdapter extends ArrayAdapter<FuturesInfo> {
    private LayoutInflater mInflater;

    public SearchAdapter(Context context, @NonNull List<FuturesInfo> futuresList) {
        super(context,0,futuresList);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder{
        TextView mTvTitle;
        TextView mTvName;
        ImageButton mBtnAdd;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FuturesInfo item = getItem(position);
        View view=convertView;
        ViewHolder holder;
        if(view==null){
            view = mInflater.inflate(R.layout.item_search_futures_result, parent, false);
            holder = new ViewHolder();
            holder.mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.mTvName = (TextView) view.findViewById(R.id.tv_name);
            holder.mBtnAdd = (ImageButton) view.findViewById(R.id.ib_add);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvTitle.setText(item.mTitle);
        holder.mTvName.setText(item.mName);
        return view;
    }

}
