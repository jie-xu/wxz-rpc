package com.github.wxz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -17:09
 */
public class IoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(IoUtils.class);

    public static String readToString(String file) throws IOException {
        return readToString(Paths.get(file));
    }

    public static String readToString(Path path) throws IOException {
        BufferedReader bufferedReader = Files.newBufferedReader(path);
        return bufferedReader.lines().collect(Collectors.joining());
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (null == closeable) {
                return;
            }
            closeable.close();
        } catch (Exception e) {
            LOGGER.error("Close closeable error", e);
        }
    }

}
