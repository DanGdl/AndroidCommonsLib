package com.mdgd.commons.retrofit_support

interface ICallback<T> {
    fun onResult(result: Result<T>)
}
