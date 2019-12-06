package com.cj.mvvmproject.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 Create by chenjiao at 2019/12/5 0005
 描述：
 */
public class Main {
    public static void main(final String[] args) {
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                System.out.println(method);
                if (method.getName().equals("morning")) {
                    System.out.println("Good morning," + args[0]);
                }
                return null;
            }
        };

        Hello hello = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, handler);
        hello.morning("Bob");
    }
}
