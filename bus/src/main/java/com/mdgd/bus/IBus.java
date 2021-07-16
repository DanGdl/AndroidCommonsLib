package com.mdgd.bus;

/**
 * Created by Owner
 * on 21/11/2018.
 */
public interface IBus {

    void notifyListeners(String key);

    void registerOnBusEventListener(IOnBusEventListener listener);

    void unregisterOnBusEventListener(IOnBusEventListener listener);

}
