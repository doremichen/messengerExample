/**
 * Remote service
 */
package com.adam.app.android.messengere.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class ConvertService extends Service {

    public static final String DATA = "data";
    public static final String RESDATA = "response data";
    public static final int TO_UPPER_CASE = 0;
    public static final int TO_UPPER_CADSE_RESPONSE = 1;

    // Remote service messenger instance
    private RemoteServiceHandler mServiceHanlder = new RemoteServiceHandler();
    private Messenger mMessenger = new Messenger(mServiceHanlder);

    
    /**
     * 
     * 
     * ConcertHandler Remote service handler
     * 
     */
    class RemoteServiceHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Utils.print(this, "[handleMessage] enter");
            int msgType = msg.what;

            switch (msgType) {
            case TO_UPPER_CASE:
                // Incoming data
                String data = msg.getData().getString(DATA);
                Message resp = Message.obtain(null, TO_UPPER_CADSE_RESPONSE);

                Bundle response = new Bundle();
                response.putString(RESDATA, data.toUpperCase());
                resp.setData(response);

                try {
                    // Send the response to UI
                    msg.replyTo.send(resp);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                super.handleMessage(msg);
                break;
            }

        }

    }

    
    
    /**
     * This method is invoked when the service is start
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.print(this, "[onCreate] enter");
    }



    /**
     * This method is invoked when the service get the command
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.print(this, "[onStartCommand] enter");
        return super.onStartCommand(intent, flags, startId);
    }



    /**
     * This method is invoked when the service is destroyed
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.print(this, "[onDestroy] enter");
    }


    /**
     * 
     * This method is invoked when the service is bounded
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        Utils.print(this, "[onBind] enter");
        return mMessenger.getBinder();
    }

}
