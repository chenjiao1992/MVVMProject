package com.cj.mvvmproject;

/**
 Create by chenjiao at 2021/3/5 0005
 描述：懒汉式,不提前准备好要的东西,用的时候才去new
 */
class SingleInstanceDemo {
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
 饿汉式
 */
class SingleInstanceDemo2 {
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
        private static final SingleInstanceDemo3 instanceDemo3 = new SingleInstanceDemo3();
    }

    private SingleInstanceDemo3() {
    }

    public static SingleInstanceDemo3 getInstancesDemo3() {
        return Hole.instanceDemo3;
    }
}