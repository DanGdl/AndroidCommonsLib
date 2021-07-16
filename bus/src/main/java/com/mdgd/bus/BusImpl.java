package com.mdgd.bus;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner
 * on 21/11/2018.
 */
public class BusImpl implements IBus {

    private final Map<IOnBusEventListener, ListenerHolder> listeners = new HashMap<>();

    @Override
    public void notifyListeners(String key) {
        for (final ListenerHolder holder : listeners.values()) {
            holder.handler.post(() -> holder.listener.onBusEvent(key));
        }
    }

    @Override
    public void registerOnBusEventListener(IOnBusEventListener listener) {
        if (listeners.get(listener) != null) return;
        Handler handler = null;
        final Looper looper = Looper.myLooper();
        for (ListenerHolder holder : listeners.values()) {
            if (holder.handler.getLooper() == looper) handler = holder.handler;
        }
        if (handler == null) handler = new Handler(looper);
        listeners.put(listener, new ListenerHolder(listener, handler));
    }

    @Override
    public void unregisterOnBusEventListener(IOnBusEventListener listener) {
        listeners.remove(listener);
    }
}
