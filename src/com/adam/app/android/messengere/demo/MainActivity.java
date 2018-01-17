/**
 * UI activity
 */
package com.adam.app.android.messengere.demo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView mTitle;
    private EditText mInput;

    private Messenger mMessenger;

    /**
     * 
     * ActivityHandler This is UI handler
     * 
     */
    private class ActivityHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            Utils.print(this, "[handleMessage] enter");
            int msgType = msg.what;

            switch (msgType) {
            case ConvertService.TO_UPPER_CADSE_RESPONSE:
                String repData = msg.getData()
                        .getString(ConvertService.RESDATA);
                mTitle.setText(repData);
                break;
            default:
                super.handleMessage(msg);
                break;
            }

        }

    }

    /**
     * Interface for monitoring the state of an application service
     */
    private ServiceConnection mConnect = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // connect to the service
            mMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.print(this, "[onCreate] enter");
        setContentView(R.layout.activity_main);

        mTitle = (TextView) this.findViewById(R.id.tv_text1);
        mInput = (EditText) this.findViewById(R.id.et_Text1);

        // Binder service
        bindService(new Intent(this, ConvertService.class), mConnect,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * This method is invoked when the ui is destroyed
     * 
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.print(this, "[onDestroy] enter");

        // Unbind service
        this.unbindService(mConnect);
        this.stopService(new Intent(this, ConvertService.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 
     * This method is invoked when the Test messenger button is pressed.
     * 
     * @param v
     */
    public void onTestBinder(View v) {

        Utils.print(this, "[onTestBinder] enter");

        // Hide soft keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (mMessenger == null) {
            Toast.makeText(this, "No service", Toast.LENGTH_SHORT).show();
            return;
        }

        String input = mInput.getText().toString();

        // Check input
        if (input.equals("")) {
            Toast.makeText(this, "Please input the charactor.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Message msg = Message.obtain(null, ConvertService.TO_UPPER_CASE);

        msg.replyTo = new Messenger(new ActivityHandler());

        Bundle b = new Bundle();
        b.putString(ConvertService.DATA, input);

        msg.setData(b);

        try {
            // Send request to the service
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
