
package com.emsg.sdk;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;

public class HeartBeatReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent mIntent) {
        EmsgClient.getInstance().getHeartBeatManager().sendHeartBeat();
    }

}
