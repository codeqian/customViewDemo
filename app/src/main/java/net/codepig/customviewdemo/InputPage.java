package net.codepig.customviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import net.codepig.customviewdemo.view.HeightProvider;

public class InputPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_page);

        EditText inputEdit=findViewById(R.id.inputEdit);
        new HeightProvider(this).init().setHeightListener(height -> {
            inputEdit.setTranslationY(-height);
        });
    }
}
