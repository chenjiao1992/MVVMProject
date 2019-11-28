package com.cj.library.base;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public interface IOtherFragmentData {
    void sendDataToOtherFragment(String receiverTag, String name, Object data);

    void onReceiveDataFromOtherFragment(String sendTag, String name, Object data);

    Object queryOtherFragmentData(String receiverTag, String name, Object data);

    Object onQueryDataFromOtherFragment(String receiverTag, String name, Object data);
}
