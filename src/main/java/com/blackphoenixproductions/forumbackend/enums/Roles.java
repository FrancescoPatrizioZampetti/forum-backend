package com.blackphoenixproductions.forumbackend.enums;

public enum Roles {


    ROLE_USER ("USER"),
    ROLE_STAFF ("STAFF"),
    ROLE_HELPDESK ("HELPDESK");

    private final String value;

    Roles(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }


}
