/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bolts.Continuation;
import bolts.Task;


public class MainActivity extends Activity {


  private Button mUnlockButton;
    private EditText mCodeText;
    private EditText mMasterKeyText;
    private TextView mMasterKeyLabel;

  private ProgressDialog progDailog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(R.layout.activity_main);


    mUnlockButton = (Button) findViewById(R.id.unlock_button);
    mCodeText = (EditText) findViewById(R.id.codeEditText);
    mMasterKeyText = (EditText) findViewById(R.id.masterKeyEditText);
      mMasterKeyLabel = (TextView) findViewById(R.id.masterKeyTextView);

      hideIfMasterKeySet();


      mUnlockButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //setProgressBarIndeterminateVisibility(true);
        if( ! UnlockManager.getInstance().hasMasterKey(MainActivity.this)) {
            UnlockManager.getInstance().setMasterKey(MainActivity.this, mMasterKeyText.getText().toString());
            hideIfMasterKeySet();
        }


        progDailog = ProgressDialog.show(MainActivity.this, "Saving", "Wait for it", true);
        UnlockManager.getInstance().deleteAll().continueWith(new Continuation<Object, Void>() {
          @Override
          public Void then(Task<Object> task) throws Exception {

            UnlockManager.getInstance().setNewCode(MainActivity.this, mCodeText.getText().toString()).continueWith(new Continuation<Void, Object>() {

                @Override
                public Object then(Task<Void> task) throws Exception {
                    //setProgressBarIndeterminateVisibility(false);
                    progDailog.dismiss();
                    return null;
                }
            });
            return null;
          }
        });



      }
    });


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

    private void hideIfMasterKeySet() {
        if(UnlockManager.getInstance().hasMasterKey(this)){
            mMasterKeyLabel.setVisibility(View.GONE);
            mMasterKeyText.setVisibility(View.GONE);
        }
    }



    @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
