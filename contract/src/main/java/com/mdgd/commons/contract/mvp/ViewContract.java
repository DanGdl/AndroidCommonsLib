package com.mdgd.commons.contract.mvp;

import com.mdgd.commons.contract.IToast;
import com.mdgd.commons.contract.progress.IProgressContainer;

/**
 * Created by Max
 * on 05/09/2018.
 */
public class ViewContract {

    public interface IPresenter {
    }

    public interface IView extends IToast, IProgressContainer {

        String getString(int id);

        String getString(int id, Object... args);

        boolean isFinishing();

        String getPackageName();

        void finish();

        void onBackPressed();
    }
}
