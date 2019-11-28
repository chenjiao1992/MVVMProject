package com.cj.library.base;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public interface IFragmentData {

    void onReceiveDataFromActivity(String name, Object data);

    void sendDataToActivity(String name, Object data);

    Object onQueryDataFromActivity(String name, Object data);

    Object queryActivityData(String name, Object data);
}
