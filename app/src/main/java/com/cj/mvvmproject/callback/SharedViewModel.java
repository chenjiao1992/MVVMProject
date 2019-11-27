package com.cj.mvvmproject.callback;

import com.cj.architecture.callback.UnPeekLiveData;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModel;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class SharedViewModel extends ViewModel {
    public final UnPeekLiveData<Boolean> timeToAddSlideListener = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> closeSlidePanelIfExpanded = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> activityCanBeClosedDirectly = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> openOrCloseDrawer = new UnPeekLiveData<>();
    public final UnPeekLiveData<Boolean> enableSwipeDrawer = new UnPeekLiveData<>();
    public static List<String> tagOfSecondaryPages = new ArrayList<>();
}
