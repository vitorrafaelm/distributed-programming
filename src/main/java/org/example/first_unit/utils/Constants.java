package org.example.first_unit.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Constants {

    // public static final String DB_IP = "100.106.207.32";
    // public static final String CLIENT_IP = "100.106.207.32";
    // public static final String LOAD_BALANCE_IP = "100.74.103.82";

    public static final String DB_IP = "localhost";
    public static final String CLIENT_IP = "localhost";
    public static final String LOAD_BALANCE_IP = "localhost";

}
