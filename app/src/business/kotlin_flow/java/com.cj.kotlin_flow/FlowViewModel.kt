package com.cj.kotlin_flow

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/**
 * @Description: java类作用描述
 * @Author: chenjiao
 * @CreateDate: 2022/11/23 13:46
 */
class FlowViewModel : ViewModel() {
    val timeFlow = flow<Int> {
        var time = 0
        while (true) {
            emit(time)
            delay(1000L)
            time++
        }
    }
}