//package com.shuai.futures.ui;
//
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnDismissListener;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.wiixiaobao.wxb.MyApplication;
//import com.wiixiaobao.wxb.R;
//import com.wiixiaobao.wxb.data.Constants;
//import com.wiixiaobao.wxb.data.Stat;
//import com.wiixiaobao.wxb.logic.UserManager;
//import com.wiixiaobao.wxb.protocol.ProtocolUtils;
//import com.wiixiaobao.wxb.protocol.SubmitCommentTask;
//import com.wiixiaobao.wxb.protocol.UploadImageTask;
//import com.wiixiaobao.wxb.ui.base.BaseActivity;
//import com.wiixiaobao.wxb.utils.NavigateUtils;
//import com.wiixiaobao.wxb.utils.Utils;
//
//import java.io.File;
//
///**
// * 发表评论
// */
//public class SubmitCommentActivity extends BaseActivity implements
//        OnClickListener {
//	private static final int REQUEST_SELECT_IMAGE=1;
//	private boolean mSubmitCommentAfterLogin;
//
//	private RequestQueue mRequestQueue;
//	private UploadImageTask mUploadImageTask;
//
//	private String mInsuranceId;
//	private String mOriginalCommentId;
//
//	private TextView mTvTitle;
//	private TextView mTvSubmit;
//	private EditText mEtComment;
//	private LinearLayout mLlSelectImage;
//	private ImageView mIvSelectImage;
//	private FrameLayout mFlImage;
//	private ImageView mIvImage;
//	private ImageView mIvDelImage;
//
//	private boolean mFromMessageCenter;
//
//	/**
//	 * 选中的图片文件路径
//	 */
//	private String mImagePath;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_submit_comment);
//
//		mRequestQueue = MyApplication.getRequestQueue();
//
//		mTvTitle=(TextView) findViewById(R.id.tv_title);
//		mTvSubmit = (TextView) findViewById(R.id.tv_submit);
//		mEtComment = (EditText) findViewById(R.id.et_comment);
//		mLlSelectImage=(LinearLayout) findViewById(R.id.ll_select_image);
//		mIvSelectImage = (ImageView) findViewById(R.id.iv_select_image);
//		mFlImage = (FrameLayout) findViewById(R.id.fl_image);
//		mIvImage = (ImageView) findViewById(R.id.iv_image);
//		mIvDelImage = (ImageView) findViewById(R.id.iv_del_image);
//
//		mFlImage.setVisibility(View.GONE);
//		mTvSubmit.setOnClickListener(this);
//		mIvSelectImage.setOnClickListener(this);
//		mIvDelImage.setOnClickListener(this);
//
//		Intent intent=getIntent();
//		mInsuranceId=intent.getStringExtra(Constants.EXTRA_INSURANCE_ID);
//		mOriginalCommentId=intent.getStringExtra(Constants.EXTRA_ORIGINAL_COMMENT_ID);
//
//		mFromMessageCenter=mInsuranceId==null;
//
//		if(mFromMessageCenter){
//			//从消息中心，"回复我的",跳过来的没有mInsuranceId
//			mTvTitle.setText("回复消息");
//			mEtComment.setHint("回复内容");
//			mLlSelectImage.setVisibility(View.GONE);
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		hideProgressDialog();
//
//		if(mUploadImageTask!=null)
//			mUploadImageTask.cancel(false);
//
//		mRequestQueue.cancelAll(this);
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		if(resultCode==RESULT_OK && requestCode==REQUEST_SELECT_IMAGE){
//				mImagePath=data.getStringExtra(SelectImageActivity.RESULT_IMAGE_PATH);
//				mFlImage.setVisibility(View.VISIBLE);
//				ImageLoader.getInstance().displayImage(Uri.fromFile(new File(mImagePath)).toString(), mIvImage);
//
//				mIvSelectImage.setVisibility(View.GONE);
//		}
//
//		if (requestCode == Constants.ACTIVITY_REQUEST_LOGIN) {
//			if (resultCode == RESULT_OK && mSubmitCommentAfterLogin) {
//				onSubmitClick();
//			}
//			mSubmitCommentAfterLogin = false;
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		int id = v.getId();
//		switch (id) {
//		case R.id.tv_submit: {
//			Stat.onEvent(mContext, Stat.EVENT_SUBMIT_COMMENT_CLICK);
//			onSubmitClick();
//
//			break;
//		}
//		case R.id.iv_del_image: {
//			Stat.onEvent(mContext, Stat.EVENT_SUBMIT_COMMENT_DELETE_IMAGE_CLICK);
//			mFlImage.setVisibility(View.GONE);
//			mIvImage.setImageDrawable(null);
//			mImagePath=null;
//
//			mIvSelectImage.setVisibility(View.VISIBLE);
//			break;
//		}
//		case R.id.iv_select_image: {
//			Stat.onEvent(mContext, Stat.EVENT_SUBMIT_COMMENT_CAMERA_CLICK);
//			Intent intent=new Intent(mContext,SelectImageActivity.class);
//			intent.putExtra(SelectImageActivity.EXTRA_UPLOADED_IMAGE, false);
//			startActivityForResult(intent, REQUEST_SELECT_IMAGE);
//			break;
//		}
//		default:
//			break;
//		}
//	}
//
//	private void onSubmitClick(){
//		final String comment = mEtComment.getEditableText().toString();
//		if(TextUtils.isEmpty(comment) && mImagePath==null){
//			Utils.showShortToast(mContext, "请输入评论内容");
//			return;
//		}
//
//		boolean logined = UserManager.getInstance().isLogined();
//		if (!logined) {
//			mSubmitCommentAfterLogin=true;
//			NavigateUtils.showLoginActivity(mContext, null, Constants.ACTIVITY_REQUEST_LOGIN);
//			return;
//		}
//
//		showProgressDialog();
//		if(mImagePath==null){
//			//纯文字
//			submitComment(comment,null);
//		}else{
//			mUploadImageTask=new UploadImageTask(mContext, mImagePath){
//
//				@Override
//				protected void onPostExecute(UploadResult result) {
//					super.onPostExecute(result);
//					if(result.getErrorCode()!=UploadResult.NO_ERROR){
//						hideProgressDialog();
//						Utils.showShortToast(mContext, result.getErrorMessage());
//						return;
//					}
//
//					submitComment(comment, result.getImageUrl());
//				}
//			};
//
//			mUploadImageTask.execute();
//		}
//	}
//
//	private void submitComment(String comment, String imageUrl) {
//		SubmitCommentTask request = new SubmitCommentTask(mContext,mInsuranceId,mOriginalCommentId, comment, imageUrl, new Listener<Void>() {
//
//					@Override
//					public void onResponse(Void response) {
//						hideProgressDialog();
//						setResult(RESULT_OK);
//
//						if(mFromMessageCenter){
//							ToastDialog dlg=new ToastDialog(mContext);
//							dlg.show();
//							dlg.setMessage("发送成功");
//							dlg.setOnDismissListener(new OnDismissListener() {
//
//								@Override
//								public void onDismiss(DialogInterface dialog) {
//									finish();
//								}
//							});
//						}else{
//							finish();
//						}
//
//					}
//				}, new ErrorListener() {
//
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						hideProgressDialog();
//						Utils.showShortToast(mContext, ProtocolUtils.getErrorInfo(error).getErrorMessage());
//					}
//
//				});
//
//		request.setTag(this);
//		mRequestQueue.add(request);
//	}
//
//}
