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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.amazonaws.demo.s3transferutility.R;

/*
 * This is the beginning screen that lets the user select if they want to upload or download
 */
public class MainActivity extends Activity {

    private Button btnDownload;
    private Button btnUpload;


    private Button btnActivity;
    private Button btnSleep;
    private Button btnPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        //btnDownload = (Button) findViewById(R.id.buttonDownloadMain);
        //btnUpload = (Button) findViewById(R.id.buttonUploadMain);

        btnActivity = (Button) findViewById(R.id.buttonActivityMain);
        btnPhone = (Button) findViewById(R.id.buttonPhoneMain);
        btnSleep = (Button) findViewById(R.id.buttonSleepMain);


        btnActivity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, ActivityActivity.class);
                startActivity(intent);
            }
        });

        btnSleep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, SleepActivity.class);
                startActivity(intent);
            }
        });

        //navigation
        btnPhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
                startActivity(intent);
            }
        });

//        btnUpload.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
