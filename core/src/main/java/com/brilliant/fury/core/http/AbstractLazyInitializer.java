package com.brilliant.fury.core.http;

import java.util.function.Supplier;

/**
 * 线程安全的延迟实例化抽象模板。
 * 只在第一次使用时才实例化，如果没有使用就不会实例化，从而提升应用启动性能。
 *
 * @author fury.
 * version 2017/10/2.
 */
public abstract class AbstractLazyInitializer<T> implements Supplier<T> {

    private T object;

    @Override
    public T get() {
        if (object == null) {
            synchronized (AbstractLazyInitializer.class) {
                if (object == null) {
                    object = initialize();
                }
            }
        }

        return object;
    }

    protected abstract T initialize();
}
