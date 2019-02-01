package net.codepig.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.codepig.customviewdemo.view.SeekRangeBar;

public class SeekRangeBarPage extends AppCompatActivity {
    private final String LOG_TAG="LOGCAT_customDEMO";
    private SeekRangeBar SeekRangeBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seek_range_bar_page);
        SeekRangeBarView=findViewById(R.id.SeekRangeBarView);
    }

    @Override
    protected void onResume(){
         super.onResume();
         if(SeekRangeBarView!=null){
             SeekRangeBarView.setEditable(true);
         }
     }
}
