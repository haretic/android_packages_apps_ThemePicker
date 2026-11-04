package com.android.customization.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.PathParser;

/**
 * Drawable that draws a grid rows x cols of icon shapes adjusting their size to fit within its
 * bounds.
 */
public class GridTileDrawable extends Drawable {

    // Path is expected configuration in following dimension: [100 x 100]))
    private static final float PATH_SIZE = 100f;
    // Space round each icon, as a share of its cell: a fixed one leaves dots in a cell as small
    // as a one-column list's
    private static final float SPACE_BETWEEN_ICONS = 0.1f;
    private final int mCols;
    private final int mRows;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mShapePath;
    private final Path mTransformedPath;
    private final Matrix mScaleMatrix;
    private float mCellSize = -1f;
    private float mSpace;
    private float mLeft;
    private float mTop;

    public GridTileDrawable(int cols, int rows, String path) {
        mCols = cols;
        mRows = rows;

        mShapePath = PathParser.createPathFromPathData(path);
        mTransformedPath = new Path(mShapePath);
        mScaleMatrix = new Matrix();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        // Square cells that fit both ways, the grid centred, so a list that is one column wide
        // isn't drawn down the left edge
        mCellSize = Math.min((float) bounds.width() / mCols, (float) bounds.height() / mRows);
        mSpace = mCellSize * SPACE_BETWEEN_ICONS;
        mLeft = bounds.left + (bounds.width() - mCellSize * mCols) / 2;
        mTop = bounds.top + (bounds.height() - mCellSize * mRows) / 2;

        float scaleFactor = (mCellSize - 2 * mSpace) / PATH_SIZE;
        mScaleMatrix.setScale(scaleFactor, scaleFactor);
        mShapePath.transform(mScaleMatrix, mTransformedPath);
    }

    @Override
    public void draw(Canvas canvas) {
        for (int r = 0; r < mRows; r++) {
            for (int c = 0; c < mCols; c++) {
                int saveCount = canvas.save();
                float y = mTop + r * mCellSize + mSpace;
                float x = mLeft + c * mCellSize + mSpace;
                canvas.translate(x, y);
                canvas.drawPath(mTransformedPath, mPaint);
                canvas.restoreToCount(saveCount);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
