package com.mdgd.commons.contract.mvp;

import com.mdgd.commons.contract.IToast;
import com.mdgd.commons.contract.fragment.FragmentContract;
import com.mdgd.commons.contract.progress.IProgressContainer;

public class ViewContract {

    public interface IPresenter<T extends FragmentContract.IHost> { }

    public interface IView extends IToast, IProgressContainer {

        String getString(int id);

        String getString(int id, Object... args);

        boolean isFinishing();

        String getPackageName();

        void finish();

        void onBackPressed();
    }
}
