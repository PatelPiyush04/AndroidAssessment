package com.theherdonline.ui.general;

import com.theherdonline.db.entity.User;

public interface InterfaceContactCard {
    void OnClickPhoneCall(User user);
    void OnClickSendEmail(User user);
    void OnClickSendMessage(User user);
    void OnClickUserAvatar(User user);

}
