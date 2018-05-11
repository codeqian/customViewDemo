package net.codepig.customviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.codepig.customviewdemo.view.flippedButton;

public class MainActivity extends AppCompatActivity {
    private flippedButton fButton;
    private final String LOG_TAG="LOGCAT_customDEMO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fButton=(flippedButton) findViewById(R.id.flippedButton);
        fButton.setOnMyClickListener(new flippedButton.IMyClick(){
            @Override
            public void onMyClick(String str) {
                Log.d(LOG_TAG,str);
            }
        });
    }
}
