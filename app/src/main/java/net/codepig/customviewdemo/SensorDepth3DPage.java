package net.codepig.customviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.codepig.customviewdemo.view.SensorDepth3D;

public class SensorDepth3DPage extends AppCompatActivity {
    private SensorDepth3D sensorDepth3D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_depth3_dpage);
        sensorDepth3D=findViewById(R.id.sensorDepth3d);
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
