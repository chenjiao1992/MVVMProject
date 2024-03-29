package com.cj.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.TextView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import com.cj.library.R
import com.cj.library.model.MenuAction
import com.cj.library.utils.MetricsUtil
import com.cj.library.utils.ViewUtil
import skin.support.content.res.SkinCompatResources
import skin.support.widget.SkinCompatToolbar

/**
 *  Create by chenjiao at 2019/12/17 0017
 *  描述：通用的toolbar
 */
open class CommonToolbar(context: Context, attrs: AttributeSet) : SkinCompatToolbar(context, attrs) {
    private var mMenuView: ActionMenuView? = null
    private var mTitleTextView: TextView? = null
    private var mTitleDrawableLeft: Drawable? = null
    private var mTitleDrawableTop: Drawable? = null
    private var mTitleDrawableRight: Drawable? = null
    private var mTitleDrawableBottom: Drawable? = null
    private var mTitleDrawablePadding: Int = 0
    private var mSubTitleTextView: TextView? = null
    private var mSubTitleDrawableLeft: Drawable? = null
    private var mSubTitleDrawableTop: Drawable? = null
    private var mSubTitleDrawableRight: Drawable? = null
    private var mSubTitleDrawableBottom: Drawable? = null
    private var mSubTitleDrawablePadding: Int = 0
    private var mSubTitlePaddingLeft = 0
    private var mSubTitlePaddingTop = 0
    private var mSubTitlePaddingRight = 0
    private var mSubTitlePaddingBottom = 0
    private var mSubTittlePadding = 0
    private var mTitleMaxWidth = 0
    private var mShowUnderLine = false
    private var mUnderLineColor = 0
    private var menuId=0
    private var mOnMenuItemClickListener: OnMenuItemClickListener? = null
    private val mActionViewClickListener: OnClickListener by lazy {
        OnClickListener {
            mOnMenuItemClickListener?.onMenuItemClick(it.getTag(R.id.key) as MenuItem)
        }
    }
    private var mSubMenus = SparseArray<List<MenuAction>>()
    var mOnTextClickListener: OnClickListener? = null
        set(value) {
            field = value
            mTitleTextView?.setOnClickListener(value)
            mSubTitleTextView?.setOnClickListener(value)
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonToolbar)
        val isImmersive = typedArray.getBoolean(R.styleable.CommonToolbar_immersive, true)
         menuId = typedArray.getResourceId(R.styleable.CommonToolbar_menu_item_text_color, 0)
        //二级标题相关
        mSubTitleDrawableLeft = typedArray.getDrawable(R.styleable.CommonToolbar_subtitle_drawableLeft)
        mSubTitleDrawableTop = typedArray.getDrawable(R.styleable.CommonToolbar_subtitle_drawableTop)
        mSubTitleDrawableRight = typedArray.getDrawable(R.styleable.CommonToolbar_subtitle_drawableRight)
        mSubTitleDrawableBottom = typedArray.getDrawable(R.styleable.CommonToolbar_subtitle_drawableBottom)
        mSubTittlePadding = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_subtitle_padding, 0)
        mSubTitleDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_subtitle_drawablePadding, 0)
        mSubTitlePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_subtitle_paddingLeft, 0)
        mSubTitlePaddingTop = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_subtitle_paddingTop, 0)
        mSubTitlePaddingRight = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_subtitle_paddingRight, 0)
        mSubTitlePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_subtitle_paddingBottom, 0)
        //一级标题相关
        mTitleDrawableLeft = typedArray.getDrawable(R.styleable.CommonToolbar_title_drawableLeft)
        mTitleDrawableTop = typedArray.getDrawable(R.styleable.CommonToolbar_title_drawableTop)
        mTitleDrawableRight = typedArray.getDrawable(R.styleable.CommonToolbar_title_drawableRight)
        mTitleDrawableBottom = typedArray.getDrawable(R.styleable.CommonToolbar_title_drawableBottom)
        mTitleDrawablePadding = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_title_drawablePadding, 0)
        mTitleMaxWidth = typedArray.getDimensionPixelSize(R.styleable.CommonToolbar_title_maxWidth, 0)
        //下划线
        mShowUnderLine = typedArray.getBoolean(R.styleable.CommonToolbar_toolbar_underLine, true)
        mUnderLineColor = typedArray.getColor(R.styleable.CommonToolbar_toolbar_underLineColor, 0)
        typedArray.recycle()

        if (isImmersive) { //如果是沉侵式
            ViewUtil.patchTopPadding(this)
        }

        adjustBounds(mSubTitleDrawableLeft)
        adjustBounds(mSubTitleDrawableTop)
        adjustBounds(mSubTitleDrawableRight)
        adjustBounds(mSubTitleDrawableBottom)
        mSubTitleTextView?.setCompoundDrawables(mSubTitleDrawableLeft, mSubTitleDrawableTop, mSubTitleDrawableRight, mSubTitleDrawableBottom)
        mSubTitleTextView?.compoundDrawablePadding = mTitleDrawablePadding

        adjustBounds(mTitleDrawableLeft)
        adjustBounds(mTitleDrawableTop)
        adjustBounds(mTitleDrawableRight)
        adjustBounds(mTitleDrawableBottom)
        mTitleTextView?.setCompoundDrawables(mTitleDrawableLeft, mTitleDrawableTop, mTitleDrawableRight, mTitleDrawableBottom)
        mTitleTextView?.compoundDrawablePadding = mTitleDrawablePadding
        if (mTitleMaxWidth > 0) {
            mTitleTextView?.maxWidth = mTitleMaxWidth
        }
        applySkin()
    }

    override fun setOnMenuItemClickListener(listener: OnMenuItemClickListener?) {
        mOnMenuItemClickListener = listener
        super.setOnMenuItemClickListener(listener)
    }

    private fun adjustBounds(drawable: Drawable?) {
        if (drawable == null) return
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    }


    fun parseMenu(menu: Menu) {
        val size = menu.size()
        mSubMenus.clear()
        for (i in 0 until size) {
            val item = menu.getItem(i)
            if (item.hasSubMenu()) {
                mSubMenus.put(item.itemId, parseMenu(item.subMenu))
            }
        }
    }

    private fun parseMenu(subMenu: SubMenu): List<MenuAction> {
        val size = subMenu.size()
        val menuList = mutableListOf<MenuAction>()
        for (i in 0 until size) {
            val item = subMenu.getItem(i)
            menuList.add(MenuAction(item.itemId, item.icon, item.title))
        }
        return menuList
    }

    fun hasSubMenu(itemId: Int) = mSubMenus[itemId] != null
    fun getSubMenu(itemId: Int) = mSubMenus[itemId]

    fun setSubTitleDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        mSubTitleDrawableLeft = left
        mSubTitleDrawableTop = top
        mSubTitleDrawableRight = right
        mSubTitleDrawableBottom = bottom
        mSubTitleTextView?.setCompoundDrawables(left, top, right, bottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val titleTextView = mTitleTextView
        if (titleTextView != null) {
            val tw = titleTextView.measuredWidth
            val tl = (measuredWidth - tw) / 2
            titleTextView.layout(tl, titleTextView.top, tl + tw, titleTextView.bottom)
        }
        val subtitleTextView = mSubTitleTextView
        if (subtitleTextView != null) {
            val stw = subtitleTextView.measuredWidth
            val stl = (measuredWidth - stw) / 2
            subtitleTextView.layout(stl, subtitleTextView.top, stl + stw, subtitleTextView.bottom)
        }
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        if (title.isNullOrEmpty()) {
            mTitleTextView = null
        } else if (mTitleTextView == null) {
            mTitleTextView = tryGetTitleTextView()
            if (mTitleMaxWidth > 0) {
                mTitleTextView?.maxWidth = mTitleMaxWidth
            }
        }
        requestLayout()
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        super.setSubtitle(subtitle)
        if (subtitle.isNullOrEmpty()) {
            mSubTitleTextView = null
        } else if (mSubTitleTextView == null) {
            mSubTitleTextView = tryGetSubTitleTextView()
        }
        requestLayout()
    }

    private fun tryGetSubTitleTextView(): TextView? {
        try {
            val field = Toolbar::class.java.getDeclaredField("mSubtitleTextView")
            field.isAccessible = true
            val subtitleTextView = field.get(this) as? TextView
            if (subtitleTextView != null) {
                subtitleTextView.setCompoundDrawables(mSubTitleDrawableLeft, mSubTitleDrawableTop, mSubTitleDrawableRight, mSubTitleDrawableBottom)
                subtitleTextView.compoundDrawablePadding = mSubTitleDrawablePadding
                subtitleTextView.setOnClickListener(mOnTextClickListener)
                if (mSubTittlePadding != 0) {
                    subtitleTextView.setPadding(mSubTittlePadding, mSubTittlePadding, mSubTittlePadding, mSubTittlePadding)
                } else {
                    subtitleTextView.setPadding(mSubTitlePaddingLeft, mSubTitlePaddingTop, mSubTitlePaddingRight, mSubTitlePaddingBottom)
                }
                return subtitleTextView
            }
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return null
    }

    private fun tryGetTitleTextView(): TextView? {
        try {
            val field = Toolbar::class.java.getDeclaredField("mTitleTextView")
            field.isAccessible = true
            val titleTextView = field.get(this) as? TextView
            if (titleTextView != null) {
                //                    titleTextView.setLines(1)
                //                    titleTextView.ellipsize = TextUtils.TruncateAt.END
                titleTextView.setCompoundDrawables(mTitleDrawableLeft, mTitleDrawableTop, mTitleDrawableRight, mTitleDrawableBottom)
                titleTextView.compoundDrawablePadding = mTitleDrawablePadding
                titleTextView.setOnClickListener(mOnTextClickListener)
                return titleTextView
            }
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return null
    }

    fun recreateMenu(menuResId: Int) {
        if (menuResId <= 0) return
        menu?.clear()
        inflateMenu(menuResId)
    }

    override fun inflateMenu(menuId: Int) {
        super.inflateMenu(menuId)
        val menu = menu ?: return
        if (menu.size() == 0) {
            mMenuView = null
        } else if (mMenuView == null) {
            mMenuView = tryGetMenuView()
        }
        splitSubMenus(menu)
        solveMenuItems()
    }

    private fun splitSubMenus(menu: Menu) {
        val subMenus = mSubMenus ?: return
        subMenus.clear()
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val actionView = item.actionView
            if (actionView != null) {
                actionView.setTag(R.id.key, item)
                actionView.setOnClickListener(mActionViewClickListener)
            }
            if (item.hasSubMenu()) {
                val subMenu = item.subMenu
                val list = ArrayList<MenuAction>()
                for (j in 0 until subMenu.size()) {
                    val subMenuItem = subMenu.getItem(j)
                    val subMenuItemId = subMenuItem.itemId
                    list.add(MenuAction(subMenuItemId, subMenuItem.icon, subMenuItem.title))
                }
                subMenus.put(item.itemId, list)
                subMenu.clear()
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (mShowUnderLine && background is ColorDrawable) {
            val color = (background as ColorDrawable).color
            if (color and 0xFF000000.toInt() == 0xFF000000.toInt() && mUnderLineColor != 0) {
                val paint = Paint()
                paint.color = mUnderLineColor
                paint.strokeWidth = MetricsUtil.getPixel(0.5F).toFloat()
                canvas?.drawLine(0f, height.toFloat() - MetricsUtil.getPixel(0.5F).toFloat(), width.toFloat(),
                        height.toFloat() - MetricsUtil.getPixel(0.5F).toFloat(), paint)
            }
        }
    }

    private fun tryGetMenuView(): ActionMenuView? {
        try {
            val field = Toolbar::class.java.getDeclaredField("mMenuView")
            field.isAccessible = true
            val menuView = field.get(this) as? ActionMenuView
            if (menuView != null) {
                return menuView
            }
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return null
    }

    private fun solveUnderLineColor() {
        if (mUnderLineColor <= 0) return
        mUnderLineColor = SkinCompatResources.getColor(context, mUnderLineColor)
        invalidate()
    }

    override fun applySkin() {
        super.applySkin()
        solveUnderLineColor()
        solveMenuItems()
    }

    private fun solveMenuItems() {
        if (menuId <= 0) return
        val menu = menu ?: return
        val menuView = mMenuView ?: return
        val context = context
        val menuItemTextColor = SkinCompatResources.getColorStateList(context, menuId)
        for (i in 0 until menuView.childCount) {
            val child = menuView.getChildAt(i)
            if (child !is TextView) continue
            child.setTextColor(menuItemTextColor)
        }
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            var icon = menuItem.icon ?: continue
            icon = DrawableCompat.wrap(icon).mutate()
            DrawableCompat.setTintList(icon, menuItemTextColor)
            menuItem.icon = icon
        }
    }
}
