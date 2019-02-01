package net.codepig.customviewdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Activity mainActivity;
    private TextView flipBtn,maskBtn,DepthBtn,flyHeartBtn,seekBarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flipBtn=findViewById(R.id.flipBtn);
        maskBtn=findViewById(R.id.maskBtn);
        DepthBtn=findViewById(R.id.DepthBtn);
        flyHeartBtn=findViewById(R.id.flyHeartBtn);
        seekBarBtn=findViewById(R.id.seekBarBtn);
        mainActivity=this;

        flipBtn.setOnClickListener(clickBtn);
        maskBtn.setOnClickListener(clickBtn);
        DepthBtn.setOnClickListener(clickBtn);
        flyHeartBtn.setOnClickListener(clickBtn);
        DepthBtn.setOnClickListener(clickBtn);
        seekBarBtn.setOnClickListener(clickBtn);
    }

    private View.OnClickListener clickBtn = new View.OnClickListener(){
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.flipBtn:
                    intent = new Intent();
                    intent.setClass(mainActivity, FlippedButtonPage.class);
                    startActivity(intent);
                    break;
                case R.id.maskBtn:
                    intent = new Intent();
                    intent.setClass(mainActivity, MaskImagePage.class);
                    startActivity(intent);
                    break;
                case R.id.DepthBtn:
                    intent = new Intent();
                    intent.setClass(mainActivity, SensorDepth3DPage.class);
                    startActivity(intent);
                    break;
                case R.id.seekBarBtn:
                    intent = new Intent();
                    intent.setClass(mainActivity, SeekRangeBarPage.class);
                    startActivity(intent);
                    break;
                case R.id.flyHeartBtn:
                    intent = new Intent();
                    intent.setClass(mainActivity, FlyHeartPage.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
