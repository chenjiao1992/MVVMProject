package com.cj.mvvmproject;

/**
 Create by chenjiao at 2021/3/5 0005
 描述：懒汉式,不提前准备好要的东西,用的时候才去new
 wp面试整理:
 1.静态内部类的手写代码,final的作用
 2.
 */
class SingleInstanceDemo {
    /**
     懒汉式在方法被调用后才创建对象,以时间换空间,在多线程下存在风险
     */
    private static SingleInstanceDemo instance;

    private SingleInstanceDemo() {
    }

    public static SingleInstanceDemo getInstance() {
        if (instance == null) {
            instance = new SingleInstanceDemo();
        }
        return instance;
    }
}

/**
 双重锁模式(Double Check Lock)简称DCL
 */
class SingleInstanceDemo4 {

    private static SingleInstanceDemo4 instance;

    private SingleInstanceDemo4() {
    }

    /**
     这个步骤在jvm里面执行分为三步:
     1.在堆内存中开辟内存空间
     2.在堆内存中实例化对象以及各个参数
     3.把对象执行堆内存空间
     由于jvm存在乱序执行功能，所以可能在2还没执行时就先执行了3，如果此时再被切换到线程B上，
     由于执行了3，INSTANCE 已经非空了，会被直接拿出来用，这样的话，就会出现异常。这个就是著名的DCL失效问题。
     */
    public static SingleInstanceDemo4 getInstance() {
        if (instance == null) {
            synchronized (SingleInstanceDemo4.class) {
                if (instance == null) {
                    instance = new SingleInstanceDemo4();
                }
            }
        }
        return instance;
    }
}


/**
 饿汉式
 */
class SingleInstanceDemo2 {
    /**
     饿汉模式在类被初始化时就已经在内存中创建了对象，以空间换时间，故不存在线程安全问题。
     */
    private static SingleInstanceDemo2 instanceDemo2 = new SingleInstanceDemo2();

    private SingleInstanceDemo2() {
    }

    public static SingleInstanceDemo2 getInstance2() {
        return instanceDemo2;
    }
}

/**
 静态内部类
 */
class SingleInstanceDemo3 {
    /**
     静态内部类在使用时才加载
     */
    private static class Hole {
        private static  SingleInstanceDemo3 instanceDemo3 = new SingleInstanceDemo3();
    }

    private SingleInstanceDemo3() {
    }

    public static SingleInstanceDemo3 getInstancesDemo3() {
        return Hole.instanceDemo3;
    }
}