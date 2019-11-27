package com.cj.mvvmproject.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;

import com.cj.mvvmproject.callback.SharedViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class BaseFragment extends Fragment {
    protected AppCompatActivity mActivity;
    protected SharedViewModel mSharedViewModel;
    protected boolean mAnimationLoaded;
    protected boolean mInitDataCame;

    private static Handler sHandler = new Handler();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedViewModel = ViewModelProviders.of(mActivity).get(SharedViewModel.class);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        sHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimationLoaded = true;
                if (mInitDataCame) {
                    loadInitData();
                }
            }
        }, 280);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private void loadInitData() {

    }

    protected NavController nav() {
        return NavHostFragment.findNavController(this);
    }

    public SharedViewModel getSharedViewModel() {
        return mSharedViewModel;
    }
}
