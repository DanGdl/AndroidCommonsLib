package com.mdgd.commons.retrofit_support

interface ITransformer<T, X> {
    fun transform(body: T?): X?
}