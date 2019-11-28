package com.cj.library.base;

/**
 Create by chenjiao at 2019/11/27 0027
 描述：
 */
public interface IActivityData {
    /**
     接受并处理来自Fragment的异步消息

     @param tag 发送消息的Fragment的标签
     @param name 消息名称
     @param data 消息内容
     */
    void onReceiveDataFromFragment(String tag, String name, Object data);

    /**
     对Fragment异步发送数据

     @param tag 目标Fragment在的标签
     @param name 消息名称
     @param data 消息内容
     */
    void sendDataToFragment(String tag, String name, Object data);

    /**
     对Fragment同步发送查询请求

     @param tag 目标Fragment在的标签
     @param name 消息名称

     @return 消息返回值
     */
    Object queryFragmentData(String tag, String name, Object data);

    Object onQueryDataFromFragment(String tag, String name, Object data);
}
