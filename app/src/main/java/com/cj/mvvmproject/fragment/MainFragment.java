package com.cj.mvvmproject.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cj.mvvmproject.R;
import com.cj.mvvmproject.base.BaseFragment;
import com.cj.mvvmproject.databinding.FragmentMainBinding;
import com.cj.mvvmproject.state.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class MainFragment extends BaseFragment {
    private MainViewModel mMainViewModel;
    private FragmentMainBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mBinding = FragmentMainBinding.bind(view);
        return view;
    }
}
