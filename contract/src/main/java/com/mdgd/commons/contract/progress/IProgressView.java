package com.mdgd.commons.contract.progress;

public interface IProgressView {

    void show();

    boolean isShowing();

    void dismiss();

    void cancel();
}
