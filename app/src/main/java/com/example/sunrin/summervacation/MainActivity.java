package com.example.sunrin.summervacation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar seekBar;
    LinearLayout linearLayout;
    TextView dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seek);
        seekBar.setMax(1000);
        linearLayout = (LinearLayout) findViewById(R.id.stroke);
        dist = (TextView) findViewById(R.id.dist);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("TAG",""+progress);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (120+progress*0.3), (int) (120+progress*0.3)));
                dist.setText(progress+"m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

}
