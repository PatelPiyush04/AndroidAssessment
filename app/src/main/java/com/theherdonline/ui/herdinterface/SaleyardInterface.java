package com.theherdonline.ui.herdinterface;

import android.widget.ImageView;

import java.util.List;

import com.theherdonline.db.entity.EntitySaleyard;

public interface SaleyardInterface {
    void OnClickSaleyard(EntitySaleyard entitySaleyard);
    void OnClickSaleyardList(List<EntitySaleyard> entitySaleyard);
    void OnSelectSaleyard(EntitySaleyard entitySaleyard);  // will be used when post a new market report page
    void onClickShareSaleyard(EntitySaleyard entitySaleyard);
    void onClickSavedSaleyard(EntitySaleyard entitySaleyard, ImageView view);
    void onUpdateSaleyard(EntitySaleyard entitySaleyard);
    void onConfirmSaleyardV2(EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard);


}
