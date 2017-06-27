package net.archeryc.radarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadarView radarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());

        setContentView(R.layout.activity_main);
        radarView = (RadarView) findViewById(R.id.radarView);

        final List<RadarUserEntity> radarUserEntities = new ArrayList<>();
        radarUserEntities.add(new RadarUserEntity());
        radarUserEntities.add(new RadarUserEntity());
        radarUserEntities.add(new RadarUserEntity());
        radarUserEntities.add(new RadarUserEntity());
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
        }, 1000);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarView.clear();

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (radarView!=null){
            radarView.clear();
        }
        super.onDestroy();
    }
}
