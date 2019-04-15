package com.mdgd.commons.support.v7.mvp;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mdgd.commons.contract.mvp.ViewContract;
import com.mdgd.commons.contract.progress.IProgressView;
import com.mdgd.commons.support.v7.R;
import com.mdgd.commons.support.v7.progress.ProgressDialogWrapper;
import com.mdgd.commons.utilities.PermissionsUtil;

/**
 * Created by Max
 * on 01/01/2018.
 */

public abstract class CommonActivity<T extends ViewContract.IPresenter> extends AppCompatActivity
        implements ViewContract.IView {
    @Deprecated
    protected boolean onForeground = false;
    private boolean hasProgress = true;
    protected final T presenter;
    private IProgressView progress;
    private boolean saveInstanceStateCalled = false;

    public CommonActivity() {
        presenter = getPresenter();
    }

    protected abstract T getPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveInstanceStateCalled = false;

        setContentView(getLayoutResId());
    }

    protected abstract int getLayoutResId();

    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceStateCalled = true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        saveInstanceStateCalled = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onForeground = true;
    }

    @Override
    protected void onPause() {
        onForeground = false;
        hideProgress();
        super.onPause();
    }

    @TargetApi(16)
    protected boolean requestPermissionsIfNeed(int requestCode, String... permissions) {
        return PermissionsUtil.requestPermissionsIfNeed(this, requestCode, permissions);
    }

    @TargetApi(16)
    protected boolean hasPermissions(String... permissions) {
        return PermissionsUtil.checkPermissions(this, permissions);
    }

    @Override
    public boolean hasProgress() {
        return hasProgress;
    }

    public void setHasProgress(boolean hasProgress) {
        this.hasProgress = hasProgress;
    }

    @Override
    public void showProgress() {
        showProgress("", "");
    }

    @Override
    public void showProgress(int titleRes, int messageRes) {
        showProgress(getString(titleRes), getString(messageRes));
    }

    @Override
    public void showProgress(String title, String message) {
        try {
            if (progress == null) progress = createProgressView(title, message);
            if (!progress.isShowing() && !isFinishing()) progress.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected IProgressView createProgressView(String title, String message) {
        return new ProgressDialogWrapper(this, title, message);
    }

    @Override
    public void hideProgress() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public void showToast(int msgRes) {
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int msgRes, String query) {
        Toast.makeText(this, getString(msgRes, query), Toast.LENGTH_SHORT).show();
    }




    @Deprecated
    protected void setFragment(Fragment fragment) {
        setFragment(fragment, false, null);
    }

    @Deprecated
    protected void setFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        if(saveInstanceStateCalled) return;
        getTransaction(addToStack, backStackTag).replace(getFragmentContainerId(), fragment).commit();
    }

    @Deprecated
    protected void setFragmentToBackStack(Fragment fragment) {
        setFragment(fragment, true, null);
    }

    protected FragmentTransaction getTransaction(boolean addToStack, String backStackTag) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToStack) transaction.addToBackStack(backStackTag);
        return transaction;
    }

    protected void addFragment(Fragment fragment) {
        addFragment(fragment, false, null);
    }

    protected void addFragmentToBackStack(Fragment fragment) {
        addFragment(fragment, true, null);
    }

    protected void addFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        if(saveInstanceStateCalled) return;
        if (fragment instanceof DialogFragment) {
            ((DialogFragment) fragment).show(getSupportFragmentManager(), backStackTag);
        } else getTransaction(addToStack, backStackTag).add(getFragmentContainerId(), fragment).commit();
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false, null);
    }

    protected void replaceFragmentToBackStack(Fragment fragment) {
        replaceFragment(fragment, true, null);
    }

    protected void replaceFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        if(saveInstanceStateCalled) return;
        getTransaction(addToStack, backStackTag).replace(getFragmentContainerId(), fragment).commit();
    }

    protected void removeFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        if(saveInstanceStateCalled) return;
        getTransaction(addToStack, backStackTag).remove(fragment).commit();
    }

    protected void removeFragment(Fragment fragment) {
        removeFragment(fragment, false, null);
    }
}
