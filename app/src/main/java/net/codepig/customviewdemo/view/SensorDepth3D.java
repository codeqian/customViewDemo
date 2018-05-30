package net.codepig.customviewdemo.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

    private int mWidth;//容器的宽度
    private int mHeight;//容器的高度

    private float[] angle = new float[3];
    private SensorManager sm;
    private String content;
    private final float NS2S = 1.0f / 1000000000.0f;// 将纳秒转化为秒
    private float timestamp;
    private Sensor mSensor;

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

        initSensor(_context,Sensor.TYPE_GYROSCOPE);//TYPE_GYROSCOPE是陀螺仪
    }

    //测量子View
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
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
    private void initSensor(Context ctx, int type) {
        sm = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mSensor = sm.getDefaultSensor(type);
        registerListener();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {//从 x、y、z 轴的正向位置观看处于原始方位的设备，设备逆时针旋转，正值；否则，为负值
            final float dT = (event.timestamp -timestamp) * NS2S;
            // 将弧度转化为角度
            float angleX = (float) Math.toDegrees(event.values[0]);
            float angleY = (float) Math.toDegrees(event.values[1]);
            float angleZ = (float) Math.toDegrees(event.values[2]);
            txt1.setText("angleX:"+angleX);
            txt2.setText("angleY:"+angleY);
            txt3.setText("angleZ:"+angleZ);
//            Log.d("LOGCAT","anglex------------>" + angleX);
//            Log.d("LOGCAT","angley------------>" + angleY);
//            Log.d("LOGCAT","anglez------------>" + angleZ);
//                Log.d("LOGCAT","gyroscopeSensor.getMinDelay()----------->" + mSensor.getMinDelay());
            timestamp = event.timestamp;
        }
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
        sm.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);//注册传感器，第一个参数为监听器，第二个是传感器类型，第三个是刷新速度
    }
}
