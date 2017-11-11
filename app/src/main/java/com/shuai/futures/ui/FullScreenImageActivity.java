package com.shuai.futures.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shuai.futures.R;
import com.shuai.futures.data.Constants;
import com.shuai.futures.ui.base.BaseActivity;
import com.shuai.futures.utils.Utils;
import com.shuai.futures.view.DoubleTapListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 全屏查看图片
 */
public class FullScreenImageActivity extends BaseActivity implements OnClickListener {
	private View mViewRoot;
	private PhotoView mPhotoView;
	private String mImageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_full_screen_image);
		
		mImageUrl=getIntent().getStringExtra(Constants.EXTRA_IMAGE_URL);
		mViewRoot=findViewById(R.id.ll_root);
		mViewRoot.setOnClickListener(this);
		
		mPhotoView=(PhotoView) findViewById(R.id.iv_image);
		
		mPhotoView.setMinimumScale(0.75f);
		mPhotoView.setMediumScale(2.0f);
		mPhotoView.setMaximumScale(3f);
		mPhotoView.setOnClickListener(this);
		mPhotoView.setOnDoubleTapListener(new DoubleTapListener((PhotoViewAttacher) mPhotoView.getIPhotoViewImplementation()));
		mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

            @Override
            public void onViewTap(View arg0, float arg1, float arg2) {
                finish();
            }
        });

        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                String[] items = {"保存图片到手机"};
                builder.setItems(items,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.saveImage(mContext, mImageUrl);
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
		
		ImageLoader.getInstance().displayImage(mImageUrl,mPhotoView);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
