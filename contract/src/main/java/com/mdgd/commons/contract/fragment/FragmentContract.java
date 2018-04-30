package com.mdgd.commons.contract.fragment;

public class FragmentContract {

    public interface IHost {
        void showProgress();

        void hideProgress();

        void finish();

        void onBackPressed();
    }

    public interface IFragment {

        boolean hasProgress();

        void showProgress();

        void hideProgress();
    }
}