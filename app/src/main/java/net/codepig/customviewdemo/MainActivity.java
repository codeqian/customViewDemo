package net.codepig.customviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.codepig.customviewdemo.view.SensorDepth3D;
import net.codepig.customviewdemo.view.flippedButton;

public class MainActivity extends AppCompatActivity {
    private SensorDepth3D sensorDepth3D;
    private final String LOG_TAG="LOGCAT_customDEMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorDepth3D=findViewById(R.id.sensorDepth3d);
//        fButton.setOnMyClickListener(new flippedButton.IMyClick(){
//            @Override
//            public void onMyClick(String str) {
//                Log.d(LOG_TAG,str);
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorDepth3D!=null){
            sensorDepth3D.unregisterListener();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(sensorDepth3D!=null){
            sensorDepth3D.registerListener();
        }
    }
}
