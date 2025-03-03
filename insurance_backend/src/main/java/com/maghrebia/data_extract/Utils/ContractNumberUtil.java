package com.maghrebia.data_extract.Utils;

import java.security.SecureRandom;

public class ContractNumberUtil {
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateContractNumber() {
        int length = 5 + RANDOM.nextInt(4); // Random length between 5 and 8
        StringBuilder contractNumber = new StringBuilder();

        if (RANDOM.nextBoolean()) {
            // 50% chance to start with a letter
            contractNumber.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }

        while (contractNumber.length() < length) {
            contractNumber.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }

        return contractNumber.toString();
    }
}
