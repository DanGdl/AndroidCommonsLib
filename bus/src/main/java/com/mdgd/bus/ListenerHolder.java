package com.mdgd.bus;

import android.os.Handler;

/**
 * Created by Owner
 * on 21/11/2018.
 */
class ListenerHolder {
    final IOnBusEventListener listener;
    final Handler handler;

    ListenerHolder(IOnBusEventListener listener, Handler handler) {
        this.listener = listener;
        this.handler = handler;
    }
}
