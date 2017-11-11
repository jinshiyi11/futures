package com.shuai.futures.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuai.futures.R;
import com.shuai.futures.data.Comment;
import com.shuai.futures.data.Constants;
import com.shuai.futures.ui.FullScreenImageActivity;
import com.shuai.futures.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 评论列表adapter
 */
public class CommentAdapter extends ArrayAdapter<Comment> {
	private Context mContext;
	private LayoutInflater mInflater;
	private OnItemClickListener mItemClickListener;
	
	public interface OnItemClickListener{
		void OnItemClick(Comment comment);
	}

	public CommentAdapter(Context context, List<Comment> objects) {
		super(context, 0, objects);
		
		mContext=context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		mItemClickListener=listener;
	}
	
	private static class ViewHolder{
		View mRoot;
		RoundedImageView mIvCommentUserHead;
		TextView mTvCommentUserName;
		TextView mTvCommentTime;
		TextView mTvCommentContent;
		ImageView mIvCommentImage;
		
		LinearLayout mLlOriginalComment;
		ImageView mIvOriginalCommentImage;
		TextView mTvOriginalCommentContent;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final Comment item = getItem(position);
		ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.item_comment, parent,false);
			holder = new ViewHolder();
			view.setTag(holder);
			holder.mRoot=view;
			
			holder.mIvCommentUserHead=(RoundedImageView) view.findViewById(R.id.iv_comment_user_head);
			holder.mTvCommentUserName=(TextView) view.findViewById(R.id.tv_comment_user_name);
			holder.mTvCommentTime=(TextView) view.findViewById(R.id.tv_comment_time);
			holder.mTvCommentContent=(TextView) view.findViewById(R.id.tv_comment_content);
			holder.mIvCommentImage=(ImageView) view.findViewById(R.id.iv_comment_image);
			
			holder.mLlOriginalComment=(LinearLayout) view.findViewById(R.id.ll_original_comment);
			holder.mIvOriginalCommentImage=(ImageView) view.findViewById(R.id.iv_original_comment_image);
			holder.mTvOriginalCommentContent=(TextView) view.findViewById(R.id.tv_original_comment_content);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItemClickListener!=null)
					mItemClickListener.OnItemClick(item);
			}
		});
		
		if(TextUtils.isEmpty(item.getCommentUserHeadUrl())){
			holder.mIvCommentUserHead.setImageResource(R.drawable.ic_default_head);
		}else{
			ImageLoader.getInstance().displayImage(item.getCommentUserHeadUrl(), holder.mIvCommentUserHead);
		}
		
		holder.mTvCommentUserName.setText(item.getCommentUserNickName());
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		holder.mTvCommentTime.setText(String.format("%1$s", format.format(new Date(item.getCommentTime()*1000))));
		if(!TextUtils.isEmpty(item.getCommentContent())){
			holder.mTvCommentContent.setVisibility(View.VISIBLE);
			holder.mTvCommentContent.setText(item.getCommentContent());
		}else{
			holder.mTvCommentContent.setVisibility(View.GONE);
		}
		
		ArrayList<String> commentImageList = item.getCommentImageList();
		if(commentImageList!=null && commentImageList.size()>0){
			holder.mIvCommentImage.setVisibility(View.VISIBLE);
			final String imageUrl=commentImageList.get(0);
			ImageLoader.getInstance().displayImage(imageUrl, holder.mIvCommentImage);
			holder.mIvCommentImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_URL, imageUrl);
                    mContext.startActivity(intent);
                }
            });

            holder.mIvCommentImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    String[] items = {"保存图片到手机"};
                    builder.setItems(items,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.saveImage(mContext,imageUrl);
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
		}else{
			holder.mIvCommentImage.setVisibility(View.GONE);
		}
		
		boolean hasOriginalComment = item.hasOriginalComment();
		if(hasOriginalComment){
			holder.mLlOriginalComment.setVisibility(View.VISIBLE);
			
			ArrayList<String> originalCommentImageList = item.getOriginalCommentImageList();
			if(originalCommentImageList!=null && originalCommentImageList.size()>0){
				holder.mIvOriginalCommentImage.setVisibility(View.VISIBLE);
				final String imageUrl=originalCommentImageList.get(0);
				ImageLoader.getInstance().displayImage(imageUrl, holder.mIvOriginalCommentImage);
				holder.mIvOriginalCommentImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(mContext, FullScreenImageActivity.class);
						intent.putExtra(Constants.EXTRA_IMAGE_URL, imageUrl);
						mContext.startActivity(intent);
					}
				});
			}else{
				holder.mIvOriginalCommentImage.setVisibility(View.GONE);
			}
			
			String nickName=Utils.getNonNullString(item.getOriginalCommentUserNickName());
			String comment=Utils.getNonNullString(item.getOriginalCommentContent());
			CharSequence html= Html.fromHtml(mContext.getString(R.string.original_comment, nickName,comment));
			holder.mTvOriginalCommentContent.setText(html);
		}else{
			holder.mLlOriginalComment.setVisibility(View.GONE);
		}
		
		return view;
	}
	

}
