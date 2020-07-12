package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayMessageActivity<i> extends AppCompatActivity {

    Switch start;
    TextView display;
    RadioButton ignite;
    SystemClock stopwatch;
    Button tempButton, primaryButton;
    Timer btnYellowTimer = new Timer();
    ArrayList<String> grid = new ArrayList<>();
    int count = 0, flag = 0;
    int interval = 5000;
    long totalTime = 0L, currTime;

    private void initButtons() {
        for (int i=2; i<=17; i++) {
            String name = "button"+ i;
            int id = getResources().getIdentifier(name, "id", getPackageName());
            findViewById(id).setEnabled(false);
        }
    }

    public void initToggleButton(){
        start = findViewById(R.id.switch1);
        start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton,boolean b){
                if (b) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ignite = findViewById(R.id.radioButton);
                            display = findViewById(R.id.textView);
                            ignite.setChecked(false);
                            display.setText("");
                        }
                    });
                    interval = 5000;
                    totalTime = 0L;
                    startTimer();
                } else {
                    initButtons();
                    btnYellowTimer.cancel();
                    System.out.println("stopped");
                }
            }
        });
    }

    private void startTimer() {
        interval -= 1000;
        System.out.println("round: " +interval);
        if(interval <= 1000) {
            System.out.println("totalTime: "+ totalTime);
            if(totalTime <= 14000){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //ignite = findViewById(R.id.radioButton);
                        display.setText("Success! In top form!");
                        ignite.setChecked(true);
                        start.setChecked(false);
                        initToggleButton();
                    }
                });
            }
            else if(totalTime <= 22500){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        display.setText("Not 100% active, be careful");
                        ignite.setChecked(true);
                        start.setChecked(false);
                        initToggleButton();
                    }
                });
            }
            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        display.setText("Get out of the vehicle \n and hail a cab Home");
                        ignite.setChecked(false);
                        start.setChecked(false);
                        initToggleButton();
                    }
                });
            }
            return;
        }
        btnYellowTimer = new Timer();
        btnYellowTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (count < 6) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            display.setText("Round "+ (5-(interval/1000)));
                        }
                    });
                    System.out.println("Timer Count:"+ count);
                    btnfunc(btnselect());
                }
                else {
                    btnYellowTimer.cancel();
                    //initButtons();
                    count=0;
                    startTimer();
                }

            }
        }, 1000, interval);
    }

    protected Button btnselect() {

        final int random = new Random().nextInt(16); // [0, 60] + 20 => [20, 80]
        Log.d("randomNumber","numberGen'd"+ random);
        String select = grid.get(random);
        System.out.println("Button selected:" + select);
        int id = getResources().getIdentifier(select, "id", getPackageName());
        System.out.println(id);
        if(flag > 0){
            if(primaryButton.isEnabled()) {
                tempButton = primaryButton;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Button not pressed loop");
                        tempButton.setEnabled(false);
                        Log.d("Button",""+tempButton.isEnabled());
                        totalTime += interval;
                        System.out.println("Added: "+ interval);
                    }
                });
            }
        }
        primaryButton = findViewById(id);
        flag++;
        Log.d("Shwetha", "I'm here");
        return primaryButton;
    }

    protected void btnfunc(final Button btn) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                count++;
                btn.setEnabled(true);
                currTime = stopwatch.currentThreadTimeMillis();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        System.out.println(currTime);
                        view.setEnabled(false);
                        if (currTime <= interval)
                            displayresult(view);
                        /*else {
                            totalTime += interval;
                            btn.setEnabled(false);
                        }*/
                    }
                });
            }
        });
    }

    private void displayresult(View v) {
        totalTime += currTime;
        //System.out.println("Added: "+ currTime);
        v.setEnabled(false);
        display.setText("Round "+ (5-(interval/1000))+ "\n" + ((Button) v).getText() + " Clicked");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_message);

        initButtons();

        String name;
        for (int i=2; i<=17; i++) {
            name = "button"+ i;
            System.out.println(name);
            grid.add(name);
        }

        display = findViewById(R.id.textView);
        initToggleButton();
    }
}
