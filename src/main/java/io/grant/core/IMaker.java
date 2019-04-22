package io.grant.core;

/**
 * 构建器
 * @param <T>
 */
public interface IMaker<T extends Object> {
    <M extends IMaker> M make(String key, Object val);
}