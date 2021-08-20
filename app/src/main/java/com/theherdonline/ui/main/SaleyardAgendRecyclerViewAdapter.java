package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemAgentCardBinding;
import com.theherdonline.db.entity.User;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.util.UIUtils;


public class SaleyardAgendRecyclerViewAdapter extends RecyclerView.Adapter<SaleyardAgendRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private final InterfaceContactCard mContactCardListener;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public SaleyardAgendRecyclerViewAdapter(Context context, List<User> items,
                                            InterfaceContactCard contactCardListener) {
        mContext = context;
        mValues = items;
        mContactCardListener = contactCardListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemAgentCardBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_agent_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User item = mValues.get(position);
        holder.mItem = item;
        UIUtils.showContactCard(mContext,holder.binding, holder.mItem,mContactCardListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User mItem;
        private final ItemAgentCardBinding binding;

        public ViewHolder(final ItemAgentCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
