package com.cj.mvvmproject.enumdemo;

/**
 Create by chenjiao at 2021/3/9 0009
 描述：
 */
enum Color {
    //枚举构造方法需要传入两个参数,而GREEN本质属于Color类,所以也需要传入
    GREEN("绿色", 0),
    RED("红色", 1),
    BLUE("蓝色", 2);
    private String name;
    private int index;

    private Color(String name, int index) { //枚举不能new 所以枚举的构造方法是私有的
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
