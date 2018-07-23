package com.mdgd.commons.contract.progress;

/**
 * Created by Owner
 * on 23/07/2018.
 */
public interface IProgressContainer {

    boolean hasProgress();

    void showProgress(String title, String message);

    void showProgress(int titleRes, int messageRes);

    void showProgress();

    void hideProgress();
}
