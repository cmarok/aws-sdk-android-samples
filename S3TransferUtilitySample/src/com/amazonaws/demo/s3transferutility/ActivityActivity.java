/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazonaws.demo.s3transferutility;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DownloadActivity displays a list of download records and a bunch of buttons
 * for managing the downloads.
 */
public class ActivityActivity extends ListActivity {

    MyReceiver myReceiver;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private final int MAX_STEPS = Constants.MAX_STEPS;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setMax(MAX_STEPS);
        textView = (TextView) findViewById(R.id.steps_taken);
        textView.setText("You have taken  steps today");


        Intent serviceIntent = new Intent(ActivityActivity.this, StepCounterService.class);
        startService(serviceIntent);
    }

    @Override
    public void onStart(){
        //Start Receiver
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StepCounterService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        super.onStart();

    }

    @Override
    public void onStop(){
        unregisterReceiver(myReceiver);
        super.onStop();
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);

            mProgressStatus = datapassed;

            mProgress.setProgress(mProgressStatus);
            textView.setText("You have taken" + datapassed + "  steps today");

        }
    }
}
