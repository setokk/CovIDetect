package com.pasoftxperts.covidetect.client.utils;

public class StringUtils
{
    public static final String ALL_SPACES = " ";

    public static String removeAllWhiteSpaces(String s)
    {
        return s.replaceAll(ALL_SPACES, "");
    }
}
