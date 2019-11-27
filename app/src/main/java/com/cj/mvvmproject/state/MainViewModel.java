package com.cj.mvvmproject.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public class MainViewModel extends ViewModel {
    public final ObservableBoolean initTabAndPage = new ObservableBoolean();
    public final ObservableField<String> pageAssetPath = new ObservableField<>();
}
