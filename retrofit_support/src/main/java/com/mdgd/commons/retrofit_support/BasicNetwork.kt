package com.mdgd.commons.retrofit_support

import retrofit2.Call

open class BasicNetwork {

    fun <T, X>execAsync(call: Call<T>, callback: ICallback<X>?, transform: ITransformer<T, X>? = null) {
        call.enqueue(RetroCallbackImpl(callback, transform))
    }

    fun <T, X>execSync(call: Call<T>, callback: ICallback<X>?, transform: ITransformer<T, X>? = null) {
        val callbackImpl = RetroCallbackImpl(callback, transform)
        try {
            callbackImpl.onResponse(call, call.execute())
        } catch (e: Throwable){
            callbackImpl.onFailure(call, e)
        }
    }
}
