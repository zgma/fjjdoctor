/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acker.simplezxing.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.acker.simplezxing.R;
import com.acker.simplezxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */

/**
 * @date 2016-11-18 9:39
 * @auther GuoJinyu
 * @description modified
 */
public final class ViewfinderView extends View {

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private static final long ANIMATION_DELAY = 20L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;
    private final Paint paint;
    private final int maskColor;
    private final int resultColor;
    private final int laserColor;
    private final int resultPointColor;
    private CameraManager cameraManager;
    private Bitmap resultBitmap;
    private int scannerAlpha;
    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;
    private boolean needDrawText = true;
    private boolean showLightImage = false;

    private boolean fullScreen;

    private Rect LightImageRect;
    private Rect LightTextRect;
    private Bitmap mImage;

    private OnLightClickListener onLightClickListener;

    int middle = -1;
    int initMiddle = 0;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            maskColor = resources.getColor(R.color.viewfinder_mask, context.getTheme());
            resultColor = resources.getColor(R.color.result_view, context.getTheme());
            laserColor = resources.getColor(R.color.viewfinder_laser, context.getTheme());
            resultPointColor = resources.getColor(R.color.possible_result_points, context.getTheme());
        } else {
            maskColor = resources.getColor(R.color.viewfinder_mask);
            resultColor = resources.getColor(R.color.result_view);
            laserColor = resources.getColor(R.color.viewfinder_laser);
            resultPointColor = resources.getColor(R.color.possible_result_points);
        }
        mImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.sdt);

        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<>(5);
        lastPossibleResultPoints = null;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public void setNeedDrawText(boolean needDrawText) {
        this.needDrawText = needDrawText;
    }

    public void setScanAreaFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        if (cameraManager == null) {
            return; // not ready yet, early draw before done configuring
        }
        Rect frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        // Draw four corner
        paint.setColor(laserColor);
        canvas.drawRect(frame.left - 10, frame.top - 10, frame.left, frame.top + 40, paint);
        canvas.drawRect(frame.left, frame.top - 10, frame.left + 40, frame.top, paint);
        canvas.drawRect(frame.right, frame.top - 10, frame.right + 10, frame.top + 40, paint);
        canvas.drawRect(frame.right - 40, frame.top - 10, frame.right, frame.top, paint);
        canvas.drawRect(frame.left - 10, frame.bottom - 40, frame.left, frame.bottom + 10, paint);
        canvas.drawRect(frame.left, frame.bottom, frame.left + 40, frame.bottom + 10, paint);
        canvas.drawRect(frame.right, frame.bottom - 40, frame.right + 10, frame.bottom + 10, paint);
        canvas.drawRect(frame.right - 40, frame.bottom, frame.right, frame.bottom + 10, paint);

        paint.setAlpha(CURRENT_POINT_OPACITY);
        canvas.drawLine(frame.left, frame.top, frame.right, frame.top, paint);
        canvas.drawLine(frame.left, frame.bottom, frame.right, frame.bottom, paint);
        canvas.drawLine(frame.left, frame.top, frame.left, frame.bottom, paint);
        canvas.drawLine(frame.right, frame.top, frame.right, frame.bottom, paint);


        if (showLightImage){
            Rect srcRect = new Rect(0, 0, mImage.getWidth(), mImage.getHeight());
            LightImageRect = new Rect((width-mImage.getWidth())/2, frame.bottom-mImage.getHeight()-60, (width-mImage.getWidth())/2+mImage.getWidth(), frame.bottom-mImage.getHeight()-60+mImage.getHeight());
            canvas.drawBitmap(mImage, srcRect, LightImageRect, paint);

            TextPaint paint = new TextPaint();
            paint.setTextSize(ViewUtil.convertSpToPixels(12, getContext()));
            paint.setColor(Color.WHITE);
            String hintLightText = getResources().getString(R.string.hint_light);

            float stringWidth = paint.measureText(hintLightText);
            float x =(getWidth()-stringWidth)/2;
            canvas.drawText(hintLightText, x, LightImageRect.bottom+40, paint);


        }

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            // Draw a red "laser scanner" line through the middle to show decoding is active
            paint.setColor(Color.RED);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            if (middle == -1){
                middle = frame.top;
            }else {
                if (middle >= frame.bottom){
                    middle = frame.top;
                }else {
                    middle = middle+10;
                }
            }
            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            float scaleX = frame.width() / (float) previewFrame.width();
            float scaleY = frame.height() / (float) previewFrame.height();

            List<ResultPoint> currentPossible = possibleResultPoints;
            List<ResultPoint> currentLast = lastPossibleResultPoints;
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new ArrayList<>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                synchronized (currentPossible) {
                    for (ResultPoint point : currentPossible) {
                        if (fullScreen)
                            canvas.drawCircle((int) (point.getX() * scaleX),
                                    (int) (point.getY() * scaleY),
                                    POINT_SIZE, paint);
                        else
                            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                                    frameTop + (int) (point.getY() * scaleY),
                                    POINT_SIZE, paint);
                    }
                }
            }
            if (currentLast != null) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                synchronized (currentLast) {
                    float radius = POINT_SIZE / 2.0f;
                    for (ResultPoint point : currentLast) {
                        if (fullScreen)
                            canvas.drawCircle((int) (point.getX() * scaleX),
                                    (int) (point.getY() * scaleY),
                                    radius, paint);
                        else
                            canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX),
                                    frameTop + (int) (point.getY() * scaleY),
                                    radius, paint);
                    }
                }
            }

            if (needDrawText) {
                TextPaint paint = new TextPaint();
                paint.setColor(Color.WHITE);
                paint.setTextSize(ViewUtil.convertSpToPixels(12, getContext()));
                StaticLayout layout = new StaticLayout(getResources().getString(R.string.hint_scan), paint, width, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, false);
                canvas.translate(0, frame.bottom + ViewUtil.convertDpToPixels(20, getContext()));
                layout.draw(canvas);
            }



            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }


    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }


    public void addPossibleResultPoint(ResultPoint point) {
        List<ResultPoint> points = possibleResultPoints;
        synchronized (points) {
            points.add(point);
            int size = points.size();
            if (size > MAX_RESULT_POINTS) {
                // trim it
                points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
            }
        }
    }


    public void setShowLightImage(boolean showLightImage) {
        this.showLightImage = showLightImage;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();
        if (showLightImage && (isRectZone(LightImageRect,(int) x,(int)y) || isRectZone(LightTextRect,(int) x,(int)y))){
            if (onLightClickListener!=null)onLightClickListener.onLightClick();
        }
        return super.onTouchEvent(event);
    }

    private boolean isRectZone(Rect rect, int x, int y) {
        return rect.contains(x, y);
    }

    public interface OnLightClickListener{
        void onLightClick();
    }

    public void setOnLightClickListener(OnLightClickListener onLightClickListener) {
        this.onLightClickListener = onLightClickListener;
    }
}


