package com.cj.chat.model.interfaces

import androidx.annotation.WorkerThread
import com.cj.chat.model.ErrorCode

interface ISendCallBack {
    @WorkerThread
    fun onSuccess(){}

    fun onError(error: ErrorCode)
}
