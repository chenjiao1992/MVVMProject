package com.cj.architecture.callback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class UnPeekLiveData<T> extends MutableLiveData<T> {
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
        hook(observer);
    }

    private void hook(Observer<? super T> observer) {
        Class<LiveData> liveDataClass = LiveData.class;
        try {
            //得到LiveData的mObservers成员变量
            Field mObservers = liveDataClass.getDeclaredField("mObservers");
            //此对象的 accessible 标志设置为指示的布尔值。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。
            // 值为 false 则指示反射的对象应该实施 Java 语言访问检查。由于JDK的安全检查耗时较多.所以通过setAccessible(true)
            // 的方式关闭安全检查就可以达到提升反射速度的目的
            mObservers.setAccessible(true);
            //获得当前对象中对应的属性值
            Object o = mObservers.get(this);
            //获得class
            Class<?> oClass = o.getClass();
            //传入的第一个参数是方法名，第二个参数名是方法参数，传入这两个参数之后，便可以根据方法名和方法参数通过反射获取带有参数的方法
            Method getMethod = oClass.getDeclaredMethod("get", Object.class);
            getMethod.setAccessible(true);
            Object objectWrapperEntry = getMethod.invoke(o, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("ObserverWrapper can not be null");
            }
            Class<?> wrapperClass = objectWrapper.getClass().getSuperclass();
            Field mLastVersion = wrapperClass.getDeclaredField("mLastVersion");
            mLastVersion.setAccessible(true);
            Field mVersion = liveDataClass.getDeclaredField("mVersion");
            mVersion.setAccessible(true);
            Object mv = mVersion.get(this);
            mLastVersion.set(objectWrapper, mv);
            mObservers.setAccessible(false);
            getMethod.setAccessible(false);
            mLastVersion.setAccessible(false);
            mVersion.setAccessible(false);

        } catch (Exception e) {
        }
    }
}

