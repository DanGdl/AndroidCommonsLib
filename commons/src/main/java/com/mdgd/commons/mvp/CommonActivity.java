package com.mdgd.commons.mvp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.mdgd.commons.contract.mvp.ViewContract;
import com.mdgd.commons.contract.progress.IProgressView;
import com.mdgd.commons.contract.progress.ProgressDialogWrapper;
import com.mdgd.commons.resources.R;

/**
 * Created by Dan
 * on 01/01/2018.
 */

public abstract class CommonActivity<T extends ViewContract.IPresenter> extends Activity
        implements ViewContract.IView {
    private boolean onForeground = false;
    protected T presenter;
    private IProgressView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(presenter == null) {
            setupPresenter();
        }
        setContentView(getLayoutResId());
    }

    protected abstract void setupPresenter();

    protected abstract int getLayoutResId();

    @Override
    protected void onStop() {
        hideProgress();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onForeground = true;
    }

    @Override
    protected void onPause(){
        onForeground = false;
        super.onPause();
    }

    @Override
    public void setPresenter(ViewContract.IPresenter presenter) {
        this.presenter = (T)presenter;
    }



    @TargetApi(16)
    protected boolean requestPermissionsIfNeed(int requestCode, String... permissions) {
        boolean result = true;
        for(String p : permissions){
            if(checkPermission(p, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED){
                result = false;
                askPermissions(requestCode, permissions);
            }
        }
        return result;
    }

    private void askPermissions(int requestCode, String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    protected boolean areAllPermissionsGranted(int[] grantResults) {
        boolean result = false;
        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }



    @Override
    public void showProgress(){
        showProgress("", getString(R.string.waiting));
    }

    @Override
    public void showProgress(String title, String message) {
        try {
            if (progress == null) {
                progress = createProgressView(title, message);
            }

            if (onForeground && !progress.isShowing()) {
                progress.show();
            }
        }
        catch (Throwable e){
            e.printStackTrace();
        }
    }

    protected IProgressView createProgressView(String title, String message) {
        return new ProgressDialogWrapper(this, title, message);
    }

    @Override
    public void hideProgress(){
        if(progress != null && progress.isShowing()){
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public void showToast(int msgRes){
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int msgRes, String query){
        Toast.makeText(this, getString(msgRes, query), Toast.LENGTH_SHORT).show();
    }



    protected void setFragment(Fragment fragment) {
        setFragment(fragment, false, null);
    }

    protected void setFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().replace(getFragmentContainerId(), fragment);
        if(addToStack){
            transaction.addToBackStack(backStackTag);
        }
        transaction.commit();
    }

    protected void setFragmentToBackStack(Fragment fragment) {
        setFragment(fragment, true, null);
    }

    protected int getFragmentContainerId(){
        return R.id.fragmentContainer;
    }
}
