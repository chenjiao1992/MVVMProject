package com.cj.library.helper;

import android.content.Context;
import android.text.TextUtils;

import com.cj.library.base.IActivityData;
import com.cj.library.base.IActivityOtherFragmentData;

import androidx.fragment.app.Fragment;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public class FragmentFrameworkHelper {
    private IActivityData mIActivityData = null;
    private IActivityOtherFragmentData mIActivityOtherFragmentData = null;

    public void onAttach(Context context) {
        if (context instanceof IActivityData) {
            mIActivityData = (IActivityData) context;
        }
        if (context instanceof IActivityOtherFragmentData) {
            mIActivityOtherFragmentData = (IActivityOtherFragmentData) context;
        }
    }

    public void onDetach() {
        mIActivityData = null;
        mIActivityOtherFragmentData = null;
    }

    public String getTag(Fragment fragment) {
        String tag = fragment.getTag();
        if (TextUtils.isEmpty(tag)) {
            return fragment.getClass().getSimpleName();
        }
        return tag;
    }

    public void sendDataToActivity(Fragment sender, String name, Object data) {
        mIActivityData.onReceiveDataFromFragment(getTag(sender), name, data);
    }

    public void sendDataToOtherFragment(Fragment sender, String receiverTag, String name, Object data) {
        mIActivityOtherFragmentData.onReceiveOtherFragmentData(getTag(sender), receiverTag, name, data);
    }

    public Object queryActivityData(Fragment query, String name, Object data) {
        return mIActivityData.onQueryDataFromFragment(getTag(query), name, data);
    }

    public Object queryOtherFragmentData(Fragment sender, String receiverTag, String name, Object data) {
        return mIActivityOtherFragmentData.onQueryOtherFragmentData(
                getTag(sender), receiverTag, name, data);
    }
}
