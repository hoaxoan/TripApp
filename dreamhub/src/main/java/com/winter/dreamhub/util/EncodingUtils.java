package com.winter.dreamhub.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by hoaxoan on 8/11/2017.
 */

public class EncodingUtils {

    public static String decodeWithPrefix(String prefix, String source) {
        try {
            return URLDecoder.decode(TextUtils.substring(source, prefix.length(), source.length()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("UTF-8 is an unknown encoding");
        }
    }

    public static String encodeWithPrefix(String prefix, String source) {
        try {
            String prefixStr = String.valueOf(prefix);
            String sourceStr = String.valueOf(URLEncoder.encode(source, "UTF-8"));
            if (sourceStr.length() != 0) {
                return prefixStr.concat(sourceStr);
            }
            return new String(prefixStr);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("UTF-8 is an unknown encoding");
        }
    }
}
