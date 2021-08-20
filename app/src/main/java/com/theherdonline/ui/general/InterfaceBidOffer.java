package com.theherdonline.ui.general;

import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.MyChat;

public interface InterfaceBidOffer {
    void OnClickBidOffer(EntityLivestock livestock);
    void OnClickMessage(MyChat chat, EntityLivestock livestock);

}
