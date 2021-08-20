package com.theherdonline.ui.home;

public enum PermissionStatus {
    GRANT(0l),
    DENIED(1l),
    DONOTASK(2l);

    private final Long id;
    PermissionStatus(Long id) { this.id = id; }
    public Long getValue() { return id; }

}
