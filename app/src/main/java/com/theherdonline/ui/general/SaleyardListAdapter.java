package com.theherdonline.ui.general;

import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemSaleyardBinding;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.util.UIUtils;

public class SaleyardListAdapter extends ArrayAdapter<EntitySaleyard> {

    private List<EntitySaleyard> mEntitySaleyardList;

   // private SaleyardPickupFragment.InterfaceSelectSaleyard mListener;
    DialogInterface.OnClickListener mClickListener;

    PickupSaleyardDialogFragment parent;
    public SaleyardListAdapter(Context context, List<EntitySaleyard> objects) {
        super(context, 0, objects);
        mEntitySaleyardList = objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemViewType(position,convertView,parent);
    }

    private View getItemViewType(final int position, View convertView, ViewGroup parent) {
        LayoutInflater a = LayoutInflater.from(parent.getContext());
        ItemSaleyardBinding mBinding = DataBindingUtil.inflate(a, R.layout.item_saleyard, parent, false);
        UIUtils.showSaleyard(getContext(), mBinding, mEntitySaleyardList.get(position), new SaleyardInterface() {
            @Override
            public void OnClickSaleyard(EntitySaleyard entitySaleyard) {

            }

            @Override
            public void OnClickSaleyardList(List<EntitySaleyard> entitySaleyard) {

            }

            @Override
            public void OnSelectSaleyard(EntitySaleyard entitySaleyard) {
             //   parent.dismiss();
            }

            @Override
            public void onClickShareSaleyard(EntitySaleyard entitySaleyard) {

            }

            @Override
            public void onClickSavedSaleyard(EntitySaleyard entitySaleyard, ImageView view) {

            }



            @Override
            public void onUpdateSaleyard(EntitySaleyard entitySaleyard) {

            }

            @Override
            public void onConfirmSaleyardV2(EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard) {

            }
        });
        return mBinding.getRoot();
    }
}
