package net.codepig.customviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.codepig.customviewdemo.R;

public class flippedButton extends LinearLayout {
    private Context mContext;
    private int mWidth;//容器的宽度
    private int mHeight;//容器的高度
    private TextView buttonText;
    private FrameLayout mButton;
    private final String LOG_TAG="LOGCAT_flippedButton";

    public flippedButton(Context context){
        super(context);
        this.mContext = context;
        init(context);
    }

    public flippedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context);
    }

    public flippedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);
    }

    private void init(Context context){
        //使用xml中的布局
        LayoutInflater.from(context).inflate(R.layout.filpped_button,this, true);
        mButton=findViewById(R.id.mButton);
        buttonText=findViewById(R.id.buttonText);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iMyClick.onMyClick("clicked me");
                flipMe();
            }
        });
    }

    //测量子View
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //遍历子元件
//        int childCount = this.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            View child = this.getChildAt(i);
//            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            int cw = child.getMeasuredWidth();
//            int ch = child.getMeasuredHeight();
//        }
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
     * 定义接口
     */
    public interface IMyClick{
        public void onMyClick(String str);
    }

    /**
     * 初始化接口变量
     */
    IMyClick iMyClick=null;

    /**
     * 自定义事件监听
     * @param _iMyClick
     */
    public void setOnMyClickListener(IMyClick _iMyClick){
        iMyClick=_iMyClick;
    }

    private void flipMe(){
        Log.d(LOG_TAG,"flip it");
    }
}
