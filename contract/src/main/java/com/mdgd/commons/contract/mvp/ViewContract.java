package com.mdgd.commons.contract.mvp;

public class ViewContract {

    public interface IPresenter {
        void onDestroy();
    }

    public interface IView {
        void setPresenter(IPresenter presenter);

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