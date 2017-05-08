package com.headlth.management.circle;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.headlth.management.R;

public class test extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prepared);
    }

    private RoundProgressBar mRoundProgressBar2;
    private int progress = 1;
    private int roundnum = 50;

    @Override
    protected void onStart() {
        super.onStart();
        RoundProgressBar bar = new RoundProgressBar(getApplicationContext(), 0);
        bar.setMmnun(90);

        mRoundProgressBar2 = (RoundProgressBar) this.findViewById(R.id.roundProgressBar2);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (progress <= roundnum) {

                    System.out.println(progress);
                    mRoundProgressBar2.setProgress(progress);
                    progress += 1;

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
