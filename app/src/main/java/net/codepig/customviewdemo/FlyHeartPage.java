package net.codepig.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.codepig.customviewdemo.view.flyHeart;

public class FlyHeartPage extends AppCompatActivity {
    private final String LOG_TAG="LOGCAT_customDEMO";
    private flyHeart flyHeartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fly_heart_page);
        flyHeartView=findViewById(R.id.flyHeartView);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(flyHeartView!=null){
            flyHeartView.LoadFlowerImage();
            flyHeartView.SetViewSize(1000,1000,300,1000);
        }
    }
}
