package com.mdgd.commons.contract.progress;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogWrapper implements IProgressView {

    private final ProgressDialog progressView;

    public ProgressDialogWrapper(Context context, String title, String message) {
        progressView = new ProgressDialog(context);
        progressView.setTitle(title);
        progressView.setMessage(message);
        progressView.setIndeterminate(true);
    }

    @Override
    public void show() {
        progressView.show();
    }

    @Override
    public boolean isShowing() {
        return progressView.isShowing();
    }

    @Override
    public void dismiss() {
        progressView.dismiss();
    }

    @Override
    public void cancel() {
        progressView.cancel();
    }
}