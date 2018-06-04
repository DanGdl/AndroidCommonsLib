package com.mdgd.commons.contract.mvp;

import com.mdgd.commons.contract.fragment.FragmentContract;

public class ViewContract {

    public interface IPresenter<T extends FragmentContract.IHost> { }

    public interface IView {

        void showProgress();

        void showProgress(String title, String message);

        void hideProgress();

        void showToast(int msgRes);

        void showToast(int msgRes, String query);

        String getString(int id);

        String getString(int id, Object... args);

        boolean isFinishing();

        String getPackageName();

        void finish();

        void onBackPressed();
    }
}
