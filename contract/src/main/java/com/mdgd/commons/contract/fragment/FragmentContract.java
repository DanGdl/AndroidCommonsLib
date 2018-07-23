package com.mdgd.commons.contract.fragment;

import com.mdgd.commons.contract.IToast;
import com.mdgd.commons.contract.progress.IProgressContainer;

public class FragmentContract {

    public interface IHost extends IProgressContainer, IToast {

        void finish();

        void onBackPressed();
    }

    public interface IPresenter {}

    public interface IFragment extends IProgressContainer {}

    public interface IView extends IProgressContainer, IToast {}
}
