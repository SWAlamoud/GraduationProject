package com.students.rooh.classes.enums;

public enum UserType {
    GUARDIAN,
    DOCTOR;

    public static UserType fromInteger(int value) {
        switch(value) {
            case 1:
                return GUARDIAN;
            case 2:
                return DOCTOR;
        }
        return GUARDIAN;
    }

    public static int toInteger(UserType value) {
        switch(value) {
            case GUARDIAN:
                return 1;
            case DOCTOR:
                return 2;
        }
        return 1;
    }
}
