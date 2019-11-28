package com.cj.library.model;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public class ActivityDataViewModel extends ViewModel {
    public MutableKVLiveData dataStore = new MutableKVLiveData<String, Object>();
    public MutableLiveData<Pair<String, Object>> commandDispatcher = new MutableLiveData<Pair<String, Object>>();
}
