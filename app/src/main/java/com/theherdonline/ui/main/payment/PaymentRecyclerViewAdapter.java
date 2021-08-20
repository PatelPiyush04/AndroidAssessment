package com.theherdonline.ui.main.payment;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemPaymentCardBinding;
import com.theherdonline.db.entity.PaymentCard;


public class PaymentRecyclerViewAdapter extends RecyclerView.Adapter<PaymentRecyclerViewAdapter.ViewHolder> {

    private List<PaymentCard> mValues;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public PaymentRecyclerViewAdapter(Context context, List<PaymentCard> items)
    {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemPaymentCardBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_payment_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PaymentCard item = mValues.get(position);
        holder.mItem = item;
        holder.binding.tvTitle.setText(mContext.getString(R.string.txt_card_ending_in_number,item.getLastFour()));
        holder.binding.tvDetail.setText(mContext.getString(R.string.txt_expires_month_year,item.getExpMonth(),item.getExpYear()));
        if (item.getBrand().toLowerCase().equals("visa"))
        {
            holder.binding.cardImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_creditcard_visa));
        }
        else if (item.getBrand().toLowerCase().equals("mastercard"))
        {
            holder.binding.cardImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_creditcard_master));
        }
        else
        {
            holder.binding.cardImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_creditcard_other));
        }
        holder.binding.executePendingBindings();
    }

    public void deleteItem(int position)
    {

    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PaymentCard mItem;
        private final ItemPaymentCardBinding binding;

        public ViewHolder(final ItemPaymentCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
