package com.melodyxxx.puredaily.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.FloatRange;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/26.
 * Description: 图片模糊类
 */

/**
 * 图片模糊类<br/>
 * 此类实现的模糊模式为覆盖模式, 即需要模糊的目标 View 应位于输入图片的上方.<br/>
 * 1: 输入图片的宽高均大于目标 View 的宽高, 此时不会处理输入图片的宽高来适应目标 View.<br/>
 * 2: 输入图片的宽或高小于目标 View 的宽或高, 此时会根据输入的图片重新生成一个新的宽高均大于目标 View 的宽高的输入
 * 图片, 以此来适应目标 View.
 */
public class Blur {

    private Context mContext;
    private Bitmap mInputBitmap;
    private View mTargetView;

    /**
     * @param context     上下文
     * @param inputBitmap 输入图片Bitmap
     * @param targetView  需要被模糊背景的目标View
     */
    public Blur(Context context, Bitmap inputBitmap, View targetView) {
        this.mContext = context;
        this.mInputBitmap = inputBitmap;
        this.mTargetView = targetView;
    }

    /**
     * @param context        上下文
     * @param inputImageView 需要使用背景作为输入图片的ImageView
     * @param targetView     需要被模糊背景的目标View
     */
    public Blur(Context context, ImageView inputImageView, View targetView) {

        inputImageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = inputImageView.getDrawingCache();
        // 取出ImageView的Bitmap
        Bitmap tempBitmap = bitmap.copy(bitmap.getConfig(), true);
        inputImageView.setDrawingCacheEnabled(false);

        this.mContext = context;
        // 设为InputBitmap
        this.mInputBitmap = tempBitmap;
        this.mTargetView = targetView;
    }

    /**
     * 不缩放原图最大化模糊背景
     */
    public void normalMaxBlur() {
        blur(1f, 25f);
    }

    /**
     * 自定义模糊背景
     *
     * @param scaleFactor 缩放大小 (>0.0), 例如若想将输入图片放大2倍然后再模糊(提高模糊效率), 则传入2.
     * @param radius      缩放半径 (0.0 , 25.0]
     */
    public void blur(
            @FloatRange(from = 0.0f, fromInclusive = false) final float scaleFactor,
            @FloatRange(from = 0.0f, to = 25f, fromInclusive = false) final float radius) {
        if (scaleFactor <= 0.0f) {
            throw new IllegalArgumentException("Value must be > 0.0 (was " + scaleFactor + ")");
        }
        if (radius <= 0.0f || radius > 25.0f) {
            throw new IllegalArgumentException("Value must be > 0.0 and ≤ 25.0 (was " + radius + ")");
        }
//        L.startTimer();
//        Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                Bitmap outputBitmap = Bitmap.createBitmap((int) (mTargetView.getMeasuredWidth() / scaleFactor),
//                        (int) (mTargetView.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(outputBitmap);
//                canvas.translate(-mTargetView.getLeft() / scaleFactor, -mTargetView.getTop() / scaleFactor);
//                canvas.scale(1 / scaleFactor, 1 / scaleFactor);
//                Paint paint = new Paint();
//                paint.setFlags(Paint.FILTER_BITMAP_FLAG);
//                canvas.drawBitmap(mInputBitmap, 0, 0, paint);
//                RenderScript rs = RenderScript.create(mContext);
//                Allocation input = Allocation.createFromBitmap(rs, outputBitmap);
//                Allocation output = Allocation.createTyped(rs, input.getType());
//                ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
//                        rs, Element.U8_4(rs));
//                blur.setRadius(radius);
//                blur.setInput(input);
//                blur.forEach(output);
//                output.copyTo(outputBitmap);
//                subscriber.onNext(outputBitmap);
//                rs.destroy();
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        mTargetView.setBackground(new BitmapDrawable(
//                                mContext.getResources(), bitmap));
//                    }
//                });

        // 调整输入图片以适应目标View
        adjustInputBitmap();

        Bitmap outputBitmap = Bitmap.createBitmap((int) (mTargetView.getMeasuredWidth() / scaleFactor),
                (int) (mTargetView.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.translate(-mTargetView.getLeft() / scaleFactor, -mTargetView.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mInputBitmap, 0, 0, paint);
        RenderScript rs = RenderScript.create(mContext);
        Allocation input = Allocation.createFromBitmap(rs, outputBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, Element.U8_4(rs));
        blur.setRadius(radius);
        blur.setInput(input);
        blur.forEach(output);
        output.copyTo(outputBitmap);
        mTargetView.setBackground(new BitmapDrawable(
                mContext.getResources(), outputBitmap));
        rs.destroy();
//        L.endTimer();
    }

    private void adjustInputBitmap() {

        float inputWidth = mInputBitmap.getWidth();
        float inputHeight = mInputBitmap.getHeight();
        float targetWidth = mTargetView.getMeasuredWidth();
        float targetHeight = mTargetView.getMeasuredHeight();

        if (inputWidth >= targetWidth && inputHeight >= targetHeight) {
            // 不需要处理
            return;
        }

        // 需要处理
        float scale = inputWidth / inputHeight;
        float dstWidth;
        float dstHeight;
        if ((targetWidth - inputWidth) >= (targetHeight - inputHeight)) {
            // 放大宽度至目标View的宽度, 并且保持比例不变
            dstWidth = targetWidth;
            dstHeight = dstWidth / scale;
        } else {
            // 放大高度至目标View的高度, 并且保持比例不变
            dstHeight = targetHeight;
            dstWidth = dstHeight / scale;
        }
        mInputBitmap = mInputBitmap.createScaledBitmap(mInputBitmap, (int) dstWidth, (int) dstHeight, true);
    }

}
