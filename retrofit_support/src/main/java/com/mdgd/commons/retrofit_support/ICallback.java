package com.mdgd.commons.retrofit_support;

/**
 * Created by Owner
 * on 05/02/2019.
 */
public interface ICallback<T> {
    void onResult(Result<T> result);
}
