package com.mdgd.commons.contract.mvp;

/**
 * Created by Dan
 * on 26/04/2018.
 */

public interface PresenterFactory<T extends ViewContract.IPresenter> {
    T create();
}
