package com.tta.geumgiri.account.domain.util;

import java.util.Random;

public class AccountRandomUtil {

    private static final Random RANDOM = new Random();

    public static String generateAccountNum(int length) {
        StringBuilder accountNum = new StringBuilder(length);

        for (int i = 0; i < 12; i++) {
            int random = RANDOM.nextInt(10);
            accountNum.append(random);
        }

        return accountNum.toString();
    }
}