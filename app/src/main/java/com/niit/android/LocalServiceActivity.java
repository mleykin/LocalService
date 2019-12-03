package com.niit.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.niit.android.LocalService.LocalBinder;

public class LocalServiceActivity extends Activity implements View.OnClickListener {
	LocalService mService;
    boolean mBound = false;
    Button buttonBind;
    Button buttonUnBind;
    Button buttonGetNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        buttonBind = (Button) findViewById(R.id.bind);
        buttonUnBind = (Button) findViewById(R.id.unbind);
        buttonGetNumber = (Button) findViewById(R.id.get_number);
        buttonBind.setOnClickListener(this);
        buttonUnBind.setOnClickListener(this);
        buttonGetNumber.setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();
        connectToService();
    }

    /** Called when a button is clicked (the button in the layout file attaches to
      * this method with the android:onClick attribute) */
    public void onClick(View v) {

    	switch (v.getId()) {
	      case R.id.bind:
	    	  connectToService();
	        break;
	      case R.id.unbind:
	    	  if (mBound) {
	              disconnectFromService();
	              Toast.makeText(getApplicationContext(), "UnBound", Toast.LENGTH_SHORT).show();
	          }
	        break;
	      case R.id.get_number:
    	    if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
               int num = mService.getRandomNumber();
               Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
    	    }
    	    else  {
    	    	Toast.makeText(this, "Not Bound", Toast.LENGTH_SHORT).show();
    	    }
    	    break;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Toast.makeText(getApplicationContext(), "Bound", Toast.LENGTH_SHORT).show();
        }


        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Toast.makeText(getApplicationContext(), "UnBound", Toast.LENGTH_SHORT).show();
        }
    };


    private void connectToService() {
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void disconnectFromService() {
        unbindService(mConnection);
    }

    protected void onStop() {
        super.onStop();
        if (mBound == true) {
            disconnectFromService();
        }
    }
}