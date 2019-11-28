package com.cj.library.base;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public interface IActivityOtherFragmentData {
    void onReceiveOtherFragmentData(String senderTag, String receiverTag, String name, Object data);

    Object onQueryOtherFragmentData(String senderTag, String receiverTag, String name, Object data);

}
