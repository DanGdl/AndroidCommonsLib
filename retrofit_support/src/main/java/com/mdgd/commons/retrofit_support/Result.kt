package com.mdgd.commons.retrofit_support

class Result<T>(val data: T? = null, val error: Throwable? = null) {

    constructor(error: Throwable? = null): this(null, error)

    fun isFail(): Boolean {
        return data == null
    }

    fun isSuccess(): Boolean {
        return error == null
    }
}
