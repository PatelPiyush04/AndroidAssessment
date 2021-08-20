package com.theherdonline.ui.herdinterface;

import java.util.List;

import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.EntitySaleyard;

public interface MapMarkListener {
    void OnClickLivestockMarkerOnMap(List<EntityLivestock> advertisement);
    void OnClickLivestockMarkerOnMap(EntityLivestock advertisement);
    void OnClickSaleyardMarkerOnMap(List<EntitySaleyard> entitySaleyard);
    void OnClickSaleyardMarkerOnMap(EntitySaleyard entitySaleyard);

}
