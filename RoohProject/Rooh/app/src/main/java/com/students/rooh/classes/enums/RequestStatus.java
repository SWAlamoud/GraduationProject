package com.students.rooh.classes.enums;

public enum RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    public static RequestStatus fromInteger(int value) {
        switch(value) {
            case 1:
                return PENDING;
            case 2:
                return ACCEPTED;
            case 3:
                return REJECTED;
        }
        return PENDING;
    }

    public static int toInteger(RequestStatus value) {
        switch(value) {
            case PENDING:
                return 1;
            case ACCEPTED:
                return 2;
            case REJECTED:
                return 3;
        }
        return 1;
    }
}
