package com.github.wxz.rpc.core.invoke.hash;

import com.github.wxz.config.RpcSystemConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * HashCriticalSection
 * FNV哈希算法
 * <p>
 * FNV能快速hash大量数据并保持较小的冲突率，
 * 它的高度分散使它适用于hash一些非常相近的字符串，
 * 比如URL，hostname，文件名，text，IP地址等
 *
 * @author xianzhi.wang
 * @date 2017/12/23 -23:03
 */
public class HashCriticalSection {
    private final static long BASIC = 0xcbf29ce484222325L;
    private final static long PRIME = 0x100000001b3L;
    private static Integer partition = RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_HASH_NUMS;
    private final Map<Integer, Semaphore> criticalSectionMap = new ConcurrentHashMap<>();

    public HashCriticalSection() {
        //1表示公平 0表示不公平
        boolean fair = RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_LOCK_FAIR == 1;
        init(null, fair);
    }

    public HashCriticalSection(Integer counts, boolean fair) {
        init(counts, fair);
    }

    public static int hash(String key) {
        return Math.abs((int) (fnv1a64(key) % partition));
    }

    /**
     * fnv1a64
     *
     * @param key
     * @return
     */
    public static long fnv1a64(String key) {
        long hashCode = BASIC;
        for (int i = 0; i < key.length(); ++i) {
            char ch = key.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                ch = (char) (ch + 32);
            }
            hashCode ^= ch;
            hashCode *= PRIME;
        }
        return hashCode;
    }

    /**
     * init
     *
     * @param counts
     * @param fair
     */
    private void init(Integer counts, boolean fair) {
        if (counts != null) {
            partition = counts;
        }
        for (int i = 0; i < partition; i++) {
            //这个多了一个参数fair表示是否是公平的，即等待时间越久的越先获取许可
            criticalSectionMap.put(i, new Semaphore(1, fair));
        }
    }

    public void enter(String key) {
        int hashKey = hash(key);
        Semaphore semaphore = criticalSectionMap.get(hashKey);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit(String key) {
        int hashKey = hash(key);
        exit(hashKey);
    }

    /**
     * acquire
     *
     * @param hashKey
     */
    public void enter(int hashKey) {
        Semaphore semaphore = criticalSectionMap.get(hashKey);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * release
     *
     * @param hashKey
     */
    public void exit(int hashKey) {
        Semaphore semaphore = criticalSectionMap.get(hashKey);
        semaphore.release();
    }
}

