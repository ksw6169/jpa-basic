package com.corgi.jpastudy.domain;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    READY("준비"),
    COMP("배송");

    private String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}
