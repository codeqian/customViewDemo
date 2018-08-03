package net.codepig.customviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.codepig.customviewdemo.R;

import java.util.List;

/**
 * 传感器控制景深效果(还未完成)
 */
public class SensorDepth3D extends LinearLayout implements SensorEventListener{
    private Context _context;
    private TextView txt1,txt2,txt3;
    private View ball0,ball1,ball2;
    private Button resetBtn;

    private int mWidth;//容器的宽度
    private int mHeight;//容器的高度

    private float[] accelerometerValues = new float[3];
    private float[] magneticValues = new float[3];
    private double[] angle = new double[3];
    private double[] angle0 = new double[3];
    private SensorManager sm;
    private String content;
    private final float NS2S = 1.0f / 1000000000.0f;// 将纳秒转化为秒
    private float timestamp;
    private Sensor gSensor;
    private Sensor mSensor;
    private float zScale=0.7f;//纵向缩放比例，值越大则场景越深
    private float dScale=85;//基础偏移量
    //中心点坐标
    private int x0=0;
    private int y0=0;

    public SensorDepth3D(Context context){
        super(context);
        this._context = context;
        init(context);
    }

    public SensorDepth3D(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        init(context);
    }

    public SensorDepth3D(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init(context);
    }

    private void init(Context mContext){
        LayoutInflater.from(mContext).inflate(R.layout.sensor_depth_3d,this, true);
        txt1=findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);
        txt3=findViewById(R.id.txt3);
        ball0=findViewById(R.id.ball0);
        ball1=findViewById(R.id.ball1);
        ball2=findViewById(R.id.ball2);
        resetBtn=findViewById(R.id.resetBtn);
        ball1.setScaleX(zScale);
        ball1.setScaleY(zScale);
        ball2.setScaleX(zScale*zScale*zScale);
        ball2.setScaleY(zScale*zScale*zScale);
        resetBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录基准角度
                angle0[0]=angle[0];
                angle0[1]=angle[1];
                angle0[2]=angle[2];
            }
        });

        initSensor(_context);
    }

    //测量子View
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if(x0==0) {
            x0 = mWidth / 2 - ball0.getMeasuredWidth() / 2;
            y0 = mHeight / 2 - ball0.getMeasuredHeight() / 2;
        }
    }

    //排列子View的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.layout(0, childTop,child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
                childTop = childTop + child.getMeasuredHeight();
            }
        }
    }

    /**
     * 获取手机所有的传感器
     * @return
     */
    private List<Sensor> initSensorS() {
        sm = (SensorManager) _context.getSystemService(Context.SENSOR_SERVICE);
        return sm.getSensorList(Sensor.TYPE_ALL);
    }

    /**
     * 根据传入的类型初始化传感器
     * @param ctx
     * @param type
     */
    private void initSensor(Context ctx) {
        sm = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        gSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//加速度传感器
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);//磁场传感器
        registerListener();
    }

    /**
     * 传感器监听
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticValues = event.values.clone();
        }

        //获取地磁与加速度传感器组合的旋转矩阵
        float[] R = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R, null, accelerometerValues,magneticValues);
        SensorManager.getOrientation(R, values);

        //values[0]->Z轴、values[1]->X轴、values[2]->Y轴
        angle[0]=values[0];
        angle[1]=values[1];
        angle[2]=values[2];
        //弧度转换为角度
        txt1.setText("angleZ:"+(int) Math.toDegrees(angle[0]));
        txt2.setText("angleX:"+(int) Math.toDegrees(angle[1]));
//        txt3.setText("angleY:"+Math.toDegrees(angle[1]));
        txt3.setText("angleY:"+(int) Math.toDegrees(values[2]));

        //计算偏移量
        double dX=dScale*Math.sin(values[2]-angle0[2]);
//        Log.d("LOGCAT","angleY"+(values[1]-angle0[1]));
        double dY=dScale*Math.sin(values[1]-angle0[1]);
//        Log.d("LOGCAT","d:"+dX+dY);
//        这里加上了屏幕中心点的位置
        ball0.setX(x0);
        ball0.setY(y0);
        ball1.setX((float) (x0-dX));
        ball1.setY((float) (y0+dY));
        ball2.setX((float) (x0-dX*2));
        ball2.setY((float) (y0+dY*2));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 精度改变
        Log.d("LOGCAT","精度改变");
    }

    /**
     * 解除监听
     */
    public void unregisterListener(){
        sm.unregisterListener(this);
    }

    public void registerListener(){
        sm.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);//注册传感器，第一个参数为监听器，第二个是传感器类型，第三个是刷新速度
        sm.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
