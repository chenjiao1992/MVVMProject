package com.cj.library.helper;

import android.text.TextUtils;

import com.cj.library.base.IActivityOtherFragmentData;
import com.cj.library.base.IFragmentData;
import com.cj.library.base.IOtherFragmentData;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 Create by chenjiao at 2019/12/4 0004
 描述：
 */
public class ActivityFrameworkHelper {
    public Fragment findFragment(Fragment fragment, String tag) {
        //getActivity 可能为 Null, 跟进源码，可以看到 getActivity() 是通过 mHost 成员变量获取 activity ，假如 mHost 为 null , 就返回一个 Null
        if (fragment.getHost() == null) {
            return null;
        }
        List<Fragment> fragmentList = fragment.getChildFragmentManager().getFragments();
        for (Fragment subFragment : fragmentList) {
            if (TextUtils.equals(subFragment.getTag(), tag)) {
                return subFragment;
            } else {
                Fragment result = findFragment(subFragment, tag);
                if (result != null) {
                    return result;
                }

            }
        }

        return null;
    }

    public Fragment getManagerFragment(FragmentActivity activity, String tag) {
        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList) {
            if (TextUtils.equals(tag, fragment.getTag())) {
                return fragment;
            }
        }
        for (Fragment fragment : fragmentList) {
            Fragment result = findFragment(fragment, tag);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public int getManagerFragmentCount(FragmentActivity activity) {
        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();
        return fragmentList.size();
    }

    public void sendDataToFragment(FragmentActivity activity, String tag, String name, Object data) {
        Fragment fragment = getManagerFragment(activity, tag);
        if (fragment == null) {
            return;
        }
        if (fragment instanceof IFragmentData) {
            ((IFragmentData) fragment).onReceiveDataFromActivity(name, data);
        }
    }

    public void onReceiveOtherFragment(FragmentActivity activity, String senderTag, String receiverTag, String name, Object data) {
        Fragment managerFragment = getManagerFragment(activity, receiverTag);
        if (managerFragment instanceof IOtherFragmentData) {
            ((IOtherFragmentData) managerFragment).onReceiveDataFromOtherFragment(senderTag, name, data);
        }
    }

    public Object queryFragmentData(FragmentActivity activity, String tag, String name, Object data) {
        Fragment managerFragment = getManagerFragment(activity, tag);
        if (managerFragment == null) {
            return null;
        }
        if (managerFragment instanceof IFragmentData) {
            ((IFragmentData) managerFragment).onQueryDataFromActivity(name, data);
        }
        return null;
    }

    public Object onQueryOtherFragmentData(FragmentActivity activity, String senderTag, String receiverTag, String name, Object data) {
        Fragment managerFragment = getManagerFragment(activity, receiverTag);
        if (managerFragment == null) {
            return null;
        }
        if (managerFragment instanceof IOtherFragmentData) {
            ((IOtherFragmentData) managerFragment).onQueryDataFromOtherFragment(senderTag, name, data);
        }
        return null;
    }
}
