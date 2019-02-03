package com.mdgd.commons.retrofitSupport

interface ICallback<T> {
    fun onResult(result: Result<T>)
}
