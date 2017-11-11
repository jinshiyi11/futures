package com.shuai.futures.view;

import android.view.MotionEvent;

import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 这个类的目的是实现1.双击图片放大，2.再双击还原
 * 因为默认实现DefaultOnDoubleTapListener的行为是1.双击图片放大，2.再双击又放大，3.再双击还原
 */
public class DoubleTapListener extends DefaultOnDoubleTapListener {
    private PhotoViewAttacher photoViewAttacher;

    public DoubleTapListener(PhotoViewAttacher attacher) {
        super(attacher);
        photoViewAttacher=attacher;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        if (photoViewAttacher == null)
            return false;

        try {
            float scale = photoViewAttacher.getScale();
            float x = ev.getX();
            float y = ev.getY();

            if (scale < 1) {
                photoViewAttacher.setScale(1, x, y, true);
            } else if (scale < photoViewAttacher.getMediumScale()) {
                photoViewAttacher.setScale(photoViewAttacher.getMediumScale(), x, y, true);
            }/*else if (scale >= photoViewAttacher.getMediumScale() && scale < photoViewAttacher.getMaximumScale()) {
                photoViewAttacher.setScale(photoViewAttacher.getMaximumScale(), x, y, true);
            } */else {
                photoViewAttacher.setScale(1.0f/*photoViewAttacher.getMinimumScale()*/, x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }

        return true;
    }
    
    

}
