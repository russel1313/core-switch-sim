package com.russel.util;

/**
 * @author Rassul Hessampour
 * @version $Revision: 1.1.0 $
 */
public class StringUtil {
    public static final String EMPTY = "";
    public static final String ZERO = "0";

    /**
     * Checks if is empty.
     *
     * @param str the str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        return (str == null) || str.trim().equals(EMPTY);
    }

    /**
     * Checks for text.
     *
     * @param str the str
     * @return true, if successful
     */
    public static boolean hasText(String str) {
        return !isEmpty(str);
    }

    /**
     * Append zero.
     *
     * @param str       the str
     * @param length    the length
     * @param alignment the alignment
     * @return the string
     */
    public static String padZero(String str, int length, Alignment alignment) {
        if (isEmpty(str)) {
            str = EMPTY;
        }
        while (str.length() < length) {
            if (alignment == Alignment.LEFT) {
                str = ZERO + str;
            } else if (alignment == Alignment.RIGHT) {
                str = str + ZERO;
            }
        }

        return str;
    }
}