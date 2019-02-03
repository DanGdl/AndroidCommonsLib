package com.mdgd.commons.retrofitSupport

interface ITransformer<T, X> {
    fun transform(body: T?): X?
}