package com.mdgd.commons.support_v7.fragment;

public class FragmentContract {

    public interface IHost {
        void showProgress();

        void hideProgress();

        void finish();
    }

    public interface IFragment {

        boolean hasProgress();

        void showProgress();

        void hideProgress();
    }
}
