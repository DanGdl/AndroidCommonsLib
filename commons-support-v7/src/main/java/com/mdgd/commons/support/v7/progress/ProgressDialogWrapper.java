package com.mdgd.commons.support.v7.progress;

import android.support.v4.app.FragmentActivity;

import com.mdgd.commons.contract.progress.IProgressView;

/**
 * Created by Max
 * on 05/09/2018.
 */
public class ProgressDialogWrapper implements IProgressView {

    private final ProgressDialogFragment progressView;
    private final FragmentActivity context;

    public ProgressDialogWrapper(FragmentActivity context, String title, String message) {
        progressView = ProgressDialogFragment.newInstance(title, message);
        this.context = context;
    }

    @Override
    public void show() {
        progressView.show(context.getSupportFragmentManager(), "progress");
    }

    @Override
    public boolean isShowing() {
        return progressView.isShowing();
    }

    @Override
    public void dismiss() {
        progressView.dismiss();
    }
}
