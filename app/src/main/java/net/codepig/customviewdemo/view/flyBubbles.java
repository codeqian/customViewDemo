package net.codepig.customviewdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.codepig.customviewdemo.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 飘动的泡泡
 * Created by QZD on 2015/8/4.
 */
public class flyBubbles extends View {
    //同屏泡泡的数量
    int bubbleMax=10;
    int view_height= 0;
    int view_width= 0;
    Bitmap bitmap_heart =null;
    //画笔
    private final Paint mPaint = new Paint();
    //位置点
    private Point[] heartPosition;
    //图片类型
    private Bitmap picType;
    //横向速度
    private float[] xSpeed;
    //纵向速度
    private float[] ySpeed;
    //缩放比例
    private float[] heartScale;
    //透明度
    private int[] heartAlpha;
    //初始位置
    private float x0,y0;
    //动画高度
    private int animHeight=200;
    //动画步数
    private int animStep=20;

    //计时器
    //进度计时器
    private Timer animTimer;
    private TimerTask animTask;
    private Handler handler;

    private int[] stepCount;

    public flyBubbles(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public flyBubbles(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        heartPosition=new Point[bubbleMax];
        heartScale=new float[bubbleMax];
        heartAlpha=new int[bubbleMax];
        xSpeed=new float[bubbleMax];
        ySpeed=new float[bubbleMax];
        stepCount=new int[bubbleMax];
    }

    public void initTimer(){
        Random random=new Random();
        for (int x = 0; x < bubbleMax; x += 1) {
            heartPosition[x]=new Point();
            resetHeart(x);
            stepCount[x]=-random.nextInt(10);
            picType=bitmap_heart;
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //定义从Task发来的任务
                    case 0:
                        drawHeart();
                        break;
                }
            }
        };

        animTask = new TimerTask() {
            @Override
            public void run() {
                if(handler!=null) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        };
        animTimer=new Timer();
        animTimer.schedule(animTask, 50, 50);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(view_height>0) {
            for (int x = 0; x < bubbleMax; x += 1) {
                Matrix matrix = new Matrix();
                float scaleWidth = heartScale[x];
                float scaleHeight = heartScale[x];
                // 缩放图片动作
                matrix.postScale(scaleWidth, scaleHeight);
                //设置画笔透明度
                mPaint.setAlpha(heartAlpha[x]);
                Bitmap _bitmap = Bitmap.createBitmap(picType, 0, 0, picType.getWidth(), picType.getHeight(),matrix, true);
                canvas.drawBitmap(_bitmap, ((float) heartPosition[x].x), ((float) heartPosition[x].y), mPaint);
            }
        }
    }

    //加载图片到内存
    public void LoadFlowerImage()
    {
        bitmap_heart= ((BitmapDrawable) this.getContext().getResources().getDrawable(R.drawable.bubble)).getBitmap();
        initTimer();
    }

    //设置窗体大小（动画描绘区域的大小）
    public void SetViewSize(int width,int height,float _x0,float _y0)
    {
        view_height=height;
        view_width=width;
        x0=_x0;
        y0=_y0;
        Log.d("LOGCAT", "heart position:" + view_width + "-" + view_height + "-" + x0 + "-" + y0);
    }

    //绘制心型
    public void drawHeart(){
        for (int x = 0; x < bubbleMax; x += 1) {
            if (stepCount[x] < 0) {
                stepCount[x]++;
                break;
            }
            //计算位置
            float _y = stepCount[x] * ySpeed[x];
            if (_y > animHeight) {
                resetHeart(x);
                _y = stepCount[x] * ySpeed[x];
            }
            float _x = stepCount[x] * xSpeed[x];
            heartPosition[x].x = (int) (x0 - _x);
            heartPosition[x].y = (int) (y0 - _y);
            //计算透明度
            float _positionPec=(animHeight-_y)/(animHeight/2);
            if(_positionPec>1){
                _positionPec=1;
            }
            heartAlpha[x]=(int) (_positionPec*100);
            stepCount[x]++;
        }
        invalidate();
    }

    //重置心型单元属性
    private void resetHeart(int _index){
        Random random = new Random();
        xSpeed[_index] = random.nextInt(6) - 3;
        ySpeed[_index] = random.nextInt(5) + 3;
        heartScale[_index] =(float) random.nextInt(5)/5 + 1;
        stepCount[_index] = 0;
    }

    //停止动画
    public void stopTimer(){
        if(handler!=null) {
            handler.removeMessages(2);
            handler = null;
        }
        if(animTimer!=null) {
            animTimer.cancel();
            animTimer=null;
        }
        if(animTask!=null) {
            animTask.cancel();
            animTask=null;
        }
    }
}
