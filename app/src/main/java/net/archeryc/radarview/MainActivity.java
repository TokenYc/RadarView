package net.archeryc.radarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RadarView radarView = (RadarView) findViewById(R.id.radarView);
        radarView.showLoading();
        radarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                radarView.stopLoading();
            }
        },3000);
    }
}
