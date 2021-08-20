package com.theherdonline.ui.main;

import javax.inject.Inject;

import com.theherdonline.di.ViewModelFactory;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class MainActivityFragment extends DaggerFragment {

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mViewModel;
}
