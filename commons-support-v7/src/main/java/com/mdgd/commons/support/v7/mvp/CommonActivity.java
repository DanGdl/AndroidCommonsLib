package com.mdgd.commons.support.v7.mvp;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mdgd.commons.contract.mvp.ViewContract;
import com.mdgd.commons.contract.progress.IProgressView;
import com.mdgd.commons.contract.progress.ProgressDialogWrapper;
import com.mdgd.commons.resources.R;
import com.mdgd.commons.utilities.PermissionsUtil;

/**
 * Created by Dan
 * on 01/01/2018.
 */

public abstract class CommonActivity<T extends ViewContract.IPresenter> extends AppCompatActivity
        implements ViewContract.IView {
    private boolean hasProgress = true;
    private boolean onForeground = false;
    protected T presenter;
    private IProgressView progress;

    public CommonActivity(){
        presenter = getPresenter();
    }

    protected abstract T getPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
    }

    protected abstract int getLayoutResId();

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
    protected void onStop() {
        hideProgress();
        super.onStop();
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
    public void showProgress(){
        showProgress("", "");
    }

    @Override
    public void showProgress(int titleRes, int messageRes) {
        showProgress(getString(titleRes), getString(messageRes));
    }

    @Override
    public void showProgress(String title, String message) {
        try {
            if (progress == null) {
                progress = createProgressView(title, message);
            }

            if (onForeground && !progress.isShowing() && !isFinishing()) {
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
        if(progress != null && progress.isShowing() && !isFinishing()){
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

    @Deprecated
    protected void setFragment(Fragment fragment) {
        setFragment(fragment, false, null);
    }

    @Deprecated
    protected void setFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(getFragmentContainerId(), fragment);
        if(addToStack){
            transaction.addToBackStack(backStackTag);
        }
        transaction.commit();
    }

    @Deprecated
    protected void setFragmentToBackStack(Fragment fragment) {
        setFragment(fragment, true, null);
    }

    protected void addFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(getFragmentContainerId(), fragment);
        if(addToStack){
            transaction.addToBackStack(backStackTag);
        }
        transaction.commit();
    }

    protected void addFragment(Fragment fragment) {
        addFragment(fragment, false, null);
    }

    protected void addFragmentToBackStack(Fragment fragment) {
        addFragment(fragment, true, null);
    }

    protected void replaceFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(getFragmentContainerId(), fragment);
        if(addToStack){
            transaction.addToBackStack(backStackTag);
        }
        transaction.commit();
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false, null);
    }

    protected void replaceFragmentToBackStack(Fragment fragment) {
        replaceFragment(fragment, true, null);
    }

    protected void removeFragment(Fragment fragment, boolean addToStack, String backStackTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(fragment);
        if(addToStack){
            transaction.addToBackStack(backStackTag);
        }
        transaction.commit();
    }

    protected void removeFragment(Fragment fragment) {
        removeFragment(fragment, false, null);
    }

    protected int getFragmentContainerId(){
        return R.id.fragmentContainer;
    }
}
