package net.codepig.customviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.util.Log;

import net.codepig.customviewdemo.R;

/**
 * 遮罩图片
 */
public class MaskImage extends AppCompatImageView {
    private int mImageSource = 0;
    private int mMaskSource = 0;
//    private RuntimeException mException;
    public MaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        //从参数获取对应的图片资源
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaskImage, 0, 0);
        mImageSource = a.getResourceId(R.styleable.MaskImage_image, 0);
        mMaskSource = a.getResourceId(R.styleable.MaskImage_mask, 0);

//        if (mImageSource == 0 || mMaskSource == 0) {
//            mException = new IllegalArgumentException(a.getPositionDescription() +
//                    ": The content attribute is required and must refer to a valid image.");
//        }
//
//        if (mException != null)
//            throw mException;

        //获取图片的bitmap
        Bitmap original = BitmapFactory.decodeResource(getResources(), mImageSource);
        Log.d("LOGCAT","pic size:"+original.getWidth()+"-"+original.getHeight());
        //获取遮罩的bitmap
        Bitmap mask = BitmapFactory.decodeResource(getResources(), mMaskSource);
        Log.d("LOGCAT","mask size:"+mask.getWidth()+"-"+mask.getHeight());
        //根据透明度创建一个新的bitmap，需要注意的是这里的第三个参数的值Bitmap.Config.ARGB_8888表示支持32位图片，也就是支持透明通道。
        Bitmap result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        //将遮罩层的图片放到画布中
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置图层混合模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //一次绘制图层
        mCanvas.drawBitmap(original, 0, 0, null);
        //计算mask的绘制比例
        Matrix mMatrix = new Matrix();
        //这里有个小坑，别忘了getWidth和getHeight的值转为float，不然算出来的也是整数。
        mMatrix.setScale((float)original.getWidth() / (float)mask.getWidth(), (float)original.getHeight() / (float)mask.getHeight());
        mCanvas.drawBitmap(mask, mMatrix, paint);
        paint.setXfermode(null);
        setImageBitmap(result);
        setScaleType(ScaleType.CENTER);
        //释放掉图片资源
        a.recycle();
    }
}
