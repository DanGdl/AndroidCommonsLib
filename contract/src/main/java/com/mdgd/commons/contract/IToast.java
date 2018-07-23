package com.mdgd.commons.contract;

/**
 * Created by Owner
 * on 23/07/2018.
 */
public interface IToast {

    void showToast(int msgRes);

    void showToast(int msgRes, String query);
}
