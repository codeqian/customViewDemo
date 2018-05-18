package net.codepig.customviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

import net.codepig.customviewdemo.R;

/**
 * 遮罩图片
 */
public class MaskImage extends AppCompatImageView {
    private int mImageSource = 0;
    private int mMaskSource = 0;
    private RuntimeException mException;
    public MaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaskImage, 0, 0);
        mImageSource = a.getResourceId(R.styleable.MaskImage_image, 0);
        mMaskSource = a.getResourceId(R.styleable.MaskImage_mask, 0);

        if (mImageSource == 0 || mMaskSource == 0) {
            mException = new IllegalArgumentException(a.getPositionDescription() +
                    ": The content attribute is required and must refer to a valid image.");
        }

        if (mException != null)
            throw mException;

        //获取图片的资源文件
        Bitmap original = BitmapFactory.decodeResource(getResources(), mImageSource);
        //获取遮罩层图片
        Bitmap mask = BitmapFactory.decodeResource(getResources(), mMaskSource);
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        //将遮罩层的图片放到画布中
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));//叠加重复的部分，显示下面的
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        setImageBitmap(result);
        setScaleType(ScaleType.CENTER);
        a.recycle();
    }
}
