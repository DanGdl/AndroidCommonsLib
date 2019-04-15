package com.mdgd.commons.support.v7.progress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.mdgd.commons.support.v7.R;

/**
 * Created by Owner
 * on 11/04/2019.
 */
public class ProgressDialogFragment extends DialogFragment {

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MSG = "key_msg";
    private static final String KEY_TITLE_STR = "key_title_str";
    private static final String KEY_MSG_STR = "key_msg_str";
    private static final String KEY_TYPE = "key_type";
    private static final int TYPE_INT = 1;
    private static final int TYPE_STR = 2;
    private boolean isShowing = false;

    public static ProgressDialogFragment newInstance(int titleResId, int msgResId) {
        final Bundle b = new Bundle();
        b.putInt(KEY_TITLE, titleResId);
        b.putInt(KEY_MSG, msgResId);
        b.putInt(KEY_TYPE, TYPE_INT);
        final ProgressDialogFragment f = new ProgressDialogFragment();
        f.setArguments(b);
        return f;
    }

    public static ProgressDialogFragment newInstance(String title, String msg) {
        final Bundle b = new Bundle();
        b.putString(KEY_TITLE_STR, title);
        b.putString(KEY_MSG_STR, msg);
        b.putInt(KEY_TYPE, TYPE_STR);
        final ProgressDialogFragment f = new ProgressDialogFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        if(args != null) {
            final int type = args.getInt(KEY_TYPE);
            if(TYPE_INT == type) {
                dialog.setTitle(getString(args.getInt(KEY_TITLE, R.string.empty)));
                dialog.setMessage(getString(args.getInt(KEY_MSG, R.string.empty)));
            } else if (TYPE_STR == type) {
                dialog.setTitle(args.getString(KEY_TITLE_STR, ""));
                dialog.setMessage(args.getString(KEY_MSG_STR, ""));
            }
        }
        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public void onStart() {
        super.onStart();
        isShowing = true;
    }

    @Override
    public void onStop() {
        isShowing = false;
        super.onStop();
    }
}
