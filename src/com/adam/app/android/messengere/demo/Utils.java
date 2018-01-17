/**
 * This is util tool
 */
package com.adam.app.android.messengere.demo;

import android.util.Log;

public final class Utils {
    
    private static final String TAG = "MessengerDemo";
    private static final boolean ISLOG = true;
    
    public static void print(Object obj, String str) {
        if (ISLOG) {
            Log.i(TAG, obj.getClass().getSimpleName() + " " + str);
        }
    }

}

/*
 * ===========================================================================
 *
 * Revision history
 *  
 * ===========================================================================
 */
