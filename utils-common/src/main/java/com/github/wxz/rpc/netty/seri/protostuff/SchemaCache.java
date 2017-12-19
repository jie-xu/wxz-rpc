package com.github.wxz.rpc.netty.seri.protostuff;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -16:38
 */
public class SchemaCache {
    private Cache<Class<?>, Schema<?>> cache = CacheBuilder.newBuilder()
            .maximumSize(1024).expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    public static SchemaCache getInstance() {
        return SchemaCacheHolder.cache;
    }

    private Schema<?> get(final Class<?> cls, Cache<Class<?>, Schema<?>> cache) {
        try {
            // return cache.get(cls, () -> RuntimeSchema.createFrom(cls));
            return cache.get(cls, new Callable<RuntimeSchema<?>>() {
                @Override
                public RuntimeSchema<?> call() throws Exception {
                    return RuntimeSchema.createFrom(cls);
                }
            });
        } catch (ExecutionException e) {
            return null;
        }
    }

    public Schema<?> get(final Class<?> cls) {
        return get(cls, cache);
    }

    private static class SchemaCacheHolder {
        private static SchemaCache cache = new SchemaCache();
    }
}

