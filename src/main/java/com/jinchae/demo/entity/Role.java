package com.jinchae.demo.entity;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_MEMBER("Member"),
    ROLE_NONE("No Authorized");

    private String viewName;

    Role(String viewName) {
        this.viewName = viewName;
    }
}
