package com.mdgd.commons.retrofitSupport

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Max
 * on 30-Apr-17.
 */

class RetroCallbackImpl<T, X>(private var callback: ICallback<X>?, private val transform: ITransformer<T, X>? = null): Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) callback?.onResult(Result(transform(response.body())))
        else callback?.onResult(Result(Exception("" + response.code() + " " + response.message())))
    }

    private fun transform(body: T?): X? {
        return if(transform == null) body as X? else transform.transform(body)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        callback?.onResult(Result(t))
    }
}