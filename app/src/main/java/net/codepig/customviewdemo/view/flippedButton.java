package net.codepig.customviewdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.codepig.customviewdemo.R;
import net.codepig.customviewdemo.model.Rotate3dAnimation;

public class flippedButton extends LinearLayout {
    private Context mContext;
    private TextView buttonText;
    private FrameLayout mButton;
    private boolean showBack=false;

    private int mWidth;//容器的宽度
    private int mHeight;//容器的高度
    private int viewsWidth;//子元素宽度
    private int viewsHeight;//子元素高度
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;

    private int centerX, centerY;
    private Rotate3dAnimation animationF;
    private Rotate3dAnimation animationB;

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
        centerX=mButton.getMeasuredWidth()/ 2;
        centerY=mButton.getMeasuredHeight() / 2;

        mWidth = 0;
        mHeight = 0;
        //margin
        marginLeft = 0;
        marginTop = 0;
        marginRight = 0;
        marginBottom = 0;
        //padding
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            viewsHeight += childView.getMeasuredHeight();
            viewsWidth = Math.max(viewsWidth, childView.getMeasuredWidth());
            marginLeft = Math.max(0,lp.leftMargin);//最大左边距
            marginTop += lp.topMargin;//上边距之和
            marginRight = Math.max(0,lp.rightMargin);//最大右边距
            marginBottom += lp.bottomMargin;//下边距之和
        }

        mWidth = getMeasuredWidth() + paddingLeft + paddingRight + marginLeft + marginRight;
        mHeight = getMeasuredHeight() + paddingBottom + paddingTop + marginTop + marginBottom;
        setMeasuredDimension(measureWidth(widthMeasureSpec, mWidth), measureHeight(heightMeasureSpec, mHeight));

        //动画
        animationF = new Rotate3dAnimation(0, 90,centerX, centerY, 0, true);
        animationF.setDuration(500);//动画持续时间，默认为0
        animationF.setFillAfter(true);//这个false的话动画完了会复原
        animationB = new Rotate3dAnimation(-90, 0,centerX, centerY, 0, true);
        animationB.setDuration(500);
        animationB.setFillAfter(true);

        animationF.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (showBack) {
                    buttonText.setText("BACK BUTTON");
                    mButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else { // 背面朝上
                    buttonText.setText("FRONT BUTTON");
                    mButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                mButton.startAnimation(animationB);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private int measureWidth(int measureSpec, int viewGroupWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                /* 将剩余宽度和所有子View + padding的值进行比较，取小的作为ViewGroup的宽度 */
                result = Math.min(viewGroupWidth, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec, int viewGroupHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                /* 将剩余高度和所有子View + padding的值进行比较，取小的作为ViewGroup的高度 */
                result = Math.min(viewGroupHeight, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
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
        // 正面朝上
        if (!showBack) {
            showBack = true;
        } else { // 背面朝上
            showBack = false;
        }
        mButton.startAnimation(animationF);
    }
}
