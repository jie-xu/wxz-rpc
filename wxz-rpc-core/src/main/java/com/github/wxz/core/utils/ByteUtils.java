package com.github.wxz.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author xianzhi.wang
 * @date 2017/12/25 -15:06
 */
public class ByteUtils {

    public static final byte[] toBytes(String text) {
        if(StringUtils.isEmpty(text)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        } else {
            try {
                return text.getBytes("utf-8");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2);
            }
        }
    }

    public static final String toString(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else if(bytes.length == 0) {
            return "";
        } else {
            try {
                return new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2);
            }
        }
    }
}
