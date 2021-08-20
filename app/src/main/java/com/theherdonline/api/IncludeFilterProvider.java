package com.theherdonline.api;

import javax.inject.Inject;

import com.theherdonline.app.AppConstants;
import com.theherdonline.di.Named;

public class IncludeFilterProvider {


    private IncludeFilter<String> mSaleyardByIdInclude;
    private IncludeFilter<String> mSaleyardInclude;
    private IncludeFilter<String> mPrivateLivestocksInclude;


    @Inject
    public IncludeFilterProvider(@Named(AppConstants.TAG_livestocks_api_include) IncludeFilter<String> mPrivateLivestocksInclude,
            @Named(AppConstants.TAG_saleyard_api_include) IncludeFilter<String> mSaleyardInclude,
                                 @Named(AppConstants.TAG_saleyard_by_id_api_include) IncludeFilter<String> mSaleyardByIdInclude)
    {
        this.mPrivateLivestocksInclude = mPrivateLivestocksInclude;
        this.mSaleyardInclude = mSaleyardInclude;
        this.mSaleyardByIdInclude = mSaleyardByIdInclude;
    }

    public IncludeFilter<String> getmSaleyardInclude() {
        return mSaleyardInclude;
    }

    public IncludeFilter<String> getmPrivateLivestocksInclude() {
        return mPrivateLivestocksInclude;
    }

    public IncludeFilter<String> getmSaleyardByIdInclude() {
        return mSaleyardByIdInclude;
    }
}
