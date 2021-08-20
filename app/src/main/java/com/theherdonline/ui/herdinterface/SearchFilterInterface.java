package com.theherdonline.ui.herdinterface;

import java.util.List;

import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.EntitySaleyard;

public interface SearchFilterInterface {
    void OnClickOpenLivestockMapView(List<EntityLivestock> list);

    void OnClickOpenSaleyardMapView(List<EntitySaleyard> list);

}
