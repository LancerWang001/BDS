package com.example.events;

public class BDSendPermitEvent {
    private boolean permission;
    public BDSendPermitEvent (boolean permission) {
        this.permission = permission;
    }

    public boolean isPermission() {
        return permission;
    }
}
