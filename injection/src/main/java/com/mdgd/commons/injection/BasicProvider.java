package com.mdgd.commons.injection;

import java.lang.ref.WeakReference;

/**
 * Created by Max
 * on 15/07/2018.
 */
public abstract class BasicProvider {

    protected WeakReference checkIfExists(WeakReference ref, IInitAction action) {
        if(ref == null || ref.get() == null){
            ref = new WeakReference<>(action.init());
        }
        return ref;
    }
}
