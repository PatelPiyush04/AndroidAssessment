package com.theherdonline.ui.main.payment;

import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.stripe.android.SourceCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.model.SourceParams;


import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentRegisterPaymentBinding;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.MainActivityViewModel;

import com.theherdonline.util.ActivityUtils;
import dagger.Lazy;


public class AddCardFragment extends HerdFragment {


   // private BasicFragment.OnFragmentInteractionListener mListener;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mViewModel;

    FragmentRegisterPaymentBinding mBinding;


    @Inject
    public AddCardFragment() {
        // Required empty public constructor
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_add_payment),mExitListener,
                null,null,null,null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_payment, container, false);
        /*mBinding.imageViewBackbutton.setOnClickListener(l->{
            mGobackListener.OnClickGoBackButton();
        });
*/
       /* UIUtils.showToolbar(getContext(),mBinding.includeToolbar,getString(R.string.txt_add_payment),mExitListener,
                null,null,null,null);*/

        mBinding.buttonSubmit.setOnClickListener(l-> {
                    String cardName = mBinding.editTextCardName.getText().toString();
                    String cardNumber = mBinding.editTextCardNumber.getText().toString();
                    String cardExpireMonth = mBinding.editTextExpMonth.getText().toString();
                    String cardExpireYear = mBinding.editTextExpYear.getText().toString();
                    String cardCvv = mBinding.editTextCvv.getText().toString();
                    if (!ActivityUtils.checkCreditCardName(getContext(), cardName, getString(R.string.txt_name_on_card))) {
                        return;
                    }
                    if (!ActivityUtils.checkCreditCardNumber(getContext(), cardNumber, getString(R.string.txt_card_number))) {
                        return;
                    }
                    if (!ActivityUtils.checkCreditCardExpireDate(getContext(), cardExpireMonth, getString(R.string.txt_card_exp_month))) {
                        return;
                    }
                    if (!ActivityUtils.checkCreditCardExpireDate(getContext(), cardExpireYear, getString(R.string.txt_card_exp_year))) {
                        return;
                    }
                    if (!ActivityUtils.checkCreditCardCvv(getContext(), cardCvv, getString(R.string.txt_card_cvv))) {
                        return;
                    }

            Card card = new Card(
                    cardNumber,
                    Integer.valueOf(cardExpireMonth),
                    Integer.valueOf(cardExpireYear),
                    cardCvv
            );
            card.setName(cardName);

            if(!card.validateNumber())
            {
                Toast.makeText(getContext(),getString(R.string.txt_str_is_invalid,getString(R.string.txt_card_number)), Toast.LENGTH_SHORT).show();
                return;

            }
            if (!card.validateCVC())
            {
                Toast.makeText(getContext(),getString(R.string.txt_str_is_invalid,getString(R.string.txt_card_cvv)), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!card.validateExpiryDate())
            {
                Toast.makeText(getContext(),getString(R.string.txt_str_is_invalid,getString(R.string.txt_card_exp_month)), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!card.validateExpMonth())
            {
                Toast.makeText(getContext(),getString(R.string.txt_str_is_invalid,getString(R.string.txt_card_exp_month)), Toast.LENGTH_SHORT).show();
                return;
            }


            SourceParams cardSourceParams = SourceParams.createCardParams(card);
// The asynchronous way to do it. Call this method on the main thread.
            Stripe mStripe = new Stripe(getContext());
            mStripe.setDefaultPublishableKey(AppConstants.STRIPE_PUBLIC_KEY);
            mNetworkListener.onLoading(true);
            mStripe.createSource(
                    cardSourceParams,
                    new SourceCallback() {
                        @Override
                        public void onError(Exception error) {
                            mNetworkListener.onLoading(false);
                            ActivityUtils.showWarningDialog(getContext(),
                                    getString(R.string.app_name),
                                    getString(R.string.txt_fail_to_verify_card) + "\n" + error.getMessage());
                        }

                        @Override
                        public void onSuccess(Source source) {
                            PaymentCard newCard = new PaymentCard();
                            newCard.setSourceId(source.getId());
                            newCard.setExpMonth(card.getExpMonth());
                            newCard.setExpYear(card.getExpYear());
                            newCard.setClientSecret(source.getClientSecret());
                            newCard.setStatus(source.getStatus());
                            newCard.setBrand(card.getBrand());
                            newCard.setLastFour(card.getLast4());
                            newCard.setStatus(source.getStatus());
                           // newCard.setThreeDSecure(source.getSourceTypeData());
                            SourceCardData sourceCardData = (SourceCardData)source.getSourceTypeModel();
                            String threeDSecure = sourceCardData.getThreeDSecureStatus();
                            newCard.setThreeDSecure(threeDSecure);

                            mViewModel.getmLDAddPaymentCard(newCard).observe(AddCardFragment.this, new DataObserver<PaymentCard>() {
                                @Override
                                public void onSuccess(PaymentCard data) {
                                    mNetworkListener.onLoading(false);
                                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_add_payment_successfully),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mGobackListener.OnClickGoBackButton();
                                                }
                                            });
                                }

                                @Override
                                public void onError(Integer code, String msg) {
                                    mNetworkListener.onLoading(false);
                                    mNetworkListener.onFailed(code,msg);

                                }

                                @Override
                                public void onLoading() {
                                    mNetworkListener.onLoading(true);

                                }

                                @Override
                                public void onDirty() {
                                    mNetworkListener.onLoading(false);

                                }
                            });

                        }
                    });







        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);


    }


}
