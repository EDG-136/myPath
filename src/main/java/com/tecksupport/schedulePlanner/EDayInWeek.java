package com.tecksupport.schedulePlanner;

public enum EDayInWeek {
    MONDAY(0),
    TUESDAY(1),
    WEDNESDAY(2),
    THURSDAY(3),
    FRIDAY(4),
    SATURDAY(5),
    SUNDAY(6);

    private final int value;
    EDayInWeek(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
