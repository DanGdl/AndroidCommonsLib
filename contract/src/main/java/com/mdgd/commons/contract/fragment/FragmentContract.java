package com.mdgd.commons.contract.fragment;

public class FragmentContract {

    public interface IHost {

        void showProgress();

        void hideProgress();

        void finish();

        void onBackPressed();

        void showToast(int msgRes);

        void showToast(int msgRes, String query);
    }

    public interface IPresenter {}

    public interface IFragment {

        boolean hasProgress();

        void showProgress();

        void hideProgress();
    }

    public interface IView<T extends FragmentContract.IHost> {

        T getIHost();

        boolean hasProgress();

        void showProgress();

        void hideProgress();

        void showToast(int msgRes);

        void showToast(int msgRes, String query);
    }
}
