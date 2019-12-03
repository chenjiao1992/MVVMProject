package com.cj.library.helper;

import android.util.Pair;

import com.cj.library.model.ActivityDataViewModel;
import com.cj.library.model.DataObserver;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public class ActivityDataHelper {
    private ActivityDataViewModel mViewModel = null;

    private ActivityDataViewModel getViewModel(LifecycleOwner owner) {
        if (mViewModel == null) {
            FragmentActivity activity = null;
            if (owner instanceof FragmentActivity) {
                activity = (FragmentActivity) owner;
            } else if (owner instanceof Fragment) {
                activity = ((Fragment) owner).getActivity();
            } else {
                activity = null;
            }

            if (activity != null) {
                mViewModel = ViewModelProviders.of(activity).get(ActivityDataViewModel.class);
            }
        }
        return mViewModel;

    }

    public void observeData(LifecycleOwner owner, String key, Observer<Object> observer) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        viewModel.dataStore.observeKey(owner, key, observer);
    }

    public void observeData(LifecycleOwner owner, DataObserver observer) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        viewModel.dataStore.observeKey(owner, observer);
    }

    public void changeData(LifecycleOwner owner, String key, Object value) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        viewModel.dataStore.putOrRemove(key, value);
    }

    public Object queryData(LifecycleOwner owner, String key) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        return viewModel.dataStore.get(key);
    }

    public void observeCommand(LifecycleOwner owner, final String key, final Observer<Object> observer) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        viewModel.commandDispatcher.observe(owner, new Observer<Pair<String, Object>>() {
            @Override
            public void onChanged(Pair<String, Object> stringObjectPair) {
                if (stringObjectPair == null) {
                    return;
                }
                if (stringObjectPair.first == key) {
                    observer.onChanged(stringObjectPair.second);
                }

            }
        });

    }

    public void observeCommand(LifecycleOwner owner, DataObserver observer) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        viewModel.commandDispatcher.observe(owner, observer);
    }

    public void dispatchCommand(LifecycleOwner owner, String name, Object data) {
        ActivityDataViewModel viewModel = getViewModel(owner);
        viewModel.commandDispatcher.setValue(new Pair<String, Object>(name, data));
        viewModel.commandDispatcher.setValue(null);
    }

    public void destroy(LifecycleOwner owner) {
        if (mViewModel == null) {
            return;
        }
        mViewModel.dataStore.removeObservers(owner);
        mViewModel.commandDispatcher.removeObservers(owner);
    }
}
