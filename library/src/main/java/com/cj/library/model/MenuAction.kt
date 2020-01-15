package com.cj.library.model

import android.graphics.drawable.Drawable

/**
 *  Create by chenjiao at 2019/12/17 0017
 *  描述：
 */
class MenuAction() {
    var menuId = 0
    var icon: Drawable? = null
    var title: CharSequence? = null
    val showIcon
        get() = icon != null

    constructor(id: Int, res: Drawable?, title: CharSequence) : this() {
        this.icon = res
        this.menuId = id
        this.title = title
    }
}