package com.github.theredbrain.bamcore.api.util;

import java.util.Arrays;

public class UUIDUtilities {

    private static final byte[] NIBBLES;
    static {
        byte[] ns = new byte[256];
        Arrays.fill(ns, (byte) -1);
        ns['0'] = 0;
        ns['1'] = 1;
        ns['2'] = 2;
        ns['3'] = 3;
        ns['4'] = 4;
        ns['5'] = 5;
        ns['6'] = 6;
        ns['7'] = 7;
        ns['8'] = 8;
        ns['9'] = 9;
        ns['A'] = 10;
        ns['B'] = 11;
        ns['C'] = 12;
        ns['D'] = 13;
        ns['E'] = 14;
        ns['F'] = 15;
        ns['a'] = 10;
        ns['b'] = 11;
        ns['c'] = 12;
        ns['d'] = 13;
        ns['e'] = 14;
        ns['f'] = 15;
        NIBBLES = ns;
    }

    private static long parse4Nibbles(String name, int pos) {
        byte[] ns = NIBBLES;
        char ch1 = name.charAt(pos);
        char ch2 = name.charAt(pos + 1);
        char ch3 = name.charAt(pos + 2);
        char ch4 = name.charAt(pos + 3);
        return (ch1 | ch2 | ch3 | ch4) > 0xff ?
                -1 : ns[ch1] << 12 | ns[ch2] << 8 | ns[ch3] << 4 | ns[ch4];
    }

    public static boolean isStringValidUUID(String string) {
        if (string.length() == 36) {
            char ch1 = string.charAt(8);
            char ch2 = string.charAt(13);
            char ch3 = string.charAt(18);
            char ch4 = string.charAt(23);
            if (ch1 == '-' && ch2 == '-' && ch3 == '-' && ch4 == '-') {
                long msb1 = parse4Nibbles(string, 0);
                long msb2 = parse4Nibbles(string, 4);
                long msb3 = parse4Nibbles(string, 9);
                long msb4 = parse4Nibbles(string, 14);
                long lsb1 = parse4Nibbles(string, 19);
                long lsb2 = parse4Nibbles(string, 24);
                long lsb3 = parse4Nibbles(string, 28);
                long lsb4 = parse4Nibbles(string, 32);
                if ((msb1 | msb2 | msb3 | msb4 | lsb1 | lsb2 | lsb3 | lsb4) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
