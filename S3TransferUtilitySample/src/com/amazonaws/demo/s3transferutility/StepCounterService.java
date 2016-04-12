package com.amazonaws.demo.s3transferutility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;
import android.provider.Settings.Secure;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import static java.security.AccessController.getContext;

public class StepCounterService extends Service implements SensorEventListener {

    private Calendar now = Calendar.getInstance();
    private int date;
    private int hour;
    private int startDate = now.get(Calendar.DATE);
    private int startHour = now.get(Calendar.HOUR_OF_DAY);

    //for filewriting
//    String android_id;
//    android_id = "1";
//    //= Secure.getString(getContext().getContentResolver(),
//     //       Secure.ANDROID_ID);
//    String filename  = "act_"+android_id + ".csv";
//    File file = new File(context.getFilesDir(), filename);
//    if (!file.exists()) {
//        file.createNewFile();
//    }
    final static String id = Secure.ANDROID_ID;
    String filename  = "act_"+id + ".csv";
    File file = new File(getApplicationContext().getFilesDir(), filename);



    final static String MY_ACTION = "MY_ACTION";
    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    private boolean isRunning;
    private int offset, current;
    private final int MAX_STEPS = Constants.MAX_STEPS;
    private int updateCheck;

    public StepCounterService() throws IOException {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = false;
        offset = 0;
        current = 0;
        updateCheck = 0;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
//        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this, sensor);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        MyThread myThread = new MyThread();
        myThread.start();
        return super.onStartCommand(intent, flags, startID);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //does nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            //when sensor changes
            //read initial value stored(sensor only resets count on hard reset of phone)
            if(!isRunning){
                offset = Math.round(event.values[0]);
                isRunning = true;
            }
            Calendar curr = Calendar.getInstance();
            int curDate = curr.get(Calendar.DATE);
            int curHour = curr.get(Calendar.HOUR_OF_DAY);

            //to reset daily at 4am next update after then
            if(curDate != startDate && curHour >= 4)
            {
                current = Math.round(event.values[0]) - offset;

                //store data to file
                String data = "";
                data = String.format("%02d", now.get(Calendar.YEAR))
                        + String.format("%02d", now.get(Calendar.MONTH) + 1)
                        + String.format("%02d", now.get(Calendar.DAY_OF_MONTH))
                        + "," + String.format("%02d", current);

                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //set the new date
                startDate = curDate;
                startHour = curHour;
                isRunning = false;
            }

            //current steps = sensor value - starting offset
            else{
                current = Math.round(event.values[0]) - offset;
            }
        }
    }

    //sends step count to main activity
    //runs until steps are equal to max steps
    public class MyThread extends Thread {

        @Override
        public void run() {

            while(current < MAX_STEPS){

                try {
                    Thread.sleep(1000);
                    if(updateCheck != current){
                        Intent intent = new Intent();
                        intent.setAction(MY_ACTION);
                        intent.putExtra("DATAPASSED", current);

                        sendBroadcast(intent);
                        updateCheck = current;
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
