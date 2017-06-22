package net.archeryc.radarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        final RadarView radarView = (RadarView) findViewById(R.id.radarView);

        final List<RadarUserEntity> radarUserEntities = new ArrayList<>();
        radarUserEntities.add(new RadarUserEntity());
        radarUserEntities.add(new RadarUserEntity());
        radarUserEntities.add(new RadarUserEntity());
        radarUserEntities.add(new RadarUserEntity());


        radarView.showLoading();
        radarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                radarView.stopLoading();
                radarView.setUserData(radarUserEntities);
            }
        }, 3000);
    }
}
