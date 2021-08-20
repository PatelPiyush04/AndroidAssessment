package com.theherdonline.ui.organisation;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemAgentBinding;
import com.theherdonline.db.entity.User;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.util.ActivityUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AgentRecyclerViewAdapter extends RecyclerView.Adapter<AgentRecyclerViewAdapter.ViewHolder> {


    private List<User> mUserList;
    private LayoutInflater layoutInflater;

    private InterfaceContactCard mInterface;

    private Context mContex;



    public AgentRecyclerViewAdapter(List<User> list,InterfaceContactCard interfaceContactCard) {
        this.mUserList = list;
        this.mInterface = interfaceContactCard;
    }

    public AgentRecyclerViewAdapter(InterfaceContactCard interfaceContactCard) {
        this.mInterface = interfaceContactCard;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());

        }
        mContex = viewGroup.getContext();
        ItemAgentBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_agent, viewGroup, false);
        return new AgentRecyclerViewAdapter.ViewHolder(binding);

    }

    public void update(List<User> userList)
    {
        mUserList = userList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ViewHolder customHolder =  viewHolder;
        customHolder.mItem = mUserList.get(i);
        ActivityUtils.loadCircleImage(mContex, customHolder.binding.imageAgentAvatar, ActivityUtils.getImageAbsoluteUrl(customHolder.mItem.getAvatar_location_full_url()), R.drawable.animal_defaul_photo);
        customHolder.binding.tvAgent.setText(StringUtils.defaultString(customHolder.mItem.getFullName()));
        customHolder.binding.getRoot().setOnClickListener(l->{
            mInterface.OnClickUserAvatar(customHolder.mItem);
        });
        }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public User mItem;
        private final ItemAgentBinding binding;

        public ViewHolder(final ItemAgentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
