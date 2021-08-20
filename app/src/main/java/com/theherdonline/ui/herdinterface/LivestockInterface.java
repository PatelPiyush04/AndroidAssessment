package com.theherdonline.ui.herdinterface;

import android.widget.ImageView;

import java.util.List;

import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Media;

public interface LivestockInterface {
    void onClickLivestock(EntityLivestock livestock);
    void onClickShareLivestock(EntityLivestock livestock);
    void onClickLivestockList(List<EntityLivestock> livestock);
    void onUpdateLivestock(EntityLivestock livestock);
    void onConfirmLivestock(EntityLivestock livestock, List<Media> mediaListToDelete, Boolean hasChanged);
    void onClickSavedLivestock(EntityLivestock livestock, ImageView view);

}
