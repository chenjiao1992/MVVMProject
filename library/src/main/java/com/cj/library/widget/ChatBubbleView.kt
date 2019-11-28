package com.cj.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import com.cj.library.R

/**
 * Created by admin on 2017/9/12.
 */
class ChatBubbleView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val PIN_DIRECTION_LEFT = 0
    private val PIN_DIRECTION_TOP = 1
    private val PIN_DIRECTION_RIGHT = 2
    private val PIN_DIRECTION_BOTTOM = 3
    private val mClipPath = Path()
    private val mCornerRadiusRect: RectF
    private val mPinDirection: Int
    private val mPinWidth: Float
    private val mPinHeight: Float
    private val mPinMargin: Float
    private val mPinTipMargin: Float
    private val mPinTipRoundRadius: Float
    private val mBorderWidth: Float
    private val mPinTipRoundRadiusRect = RectF()
    private val mPaint: Paint
    private val mDrawShadow: Boolean
    private val mShadowColor: Int
    private val mShadowDx: Float
    private val mShadowDy: Float
    private val mShadowPath = Path()
    private val mShadowPaint: Paint

    init {
        val style = context.obtainStyledAttributes(attrs, R.styleable.ChatBubbleView)
        if (style.hasValue(R.styleable.ChatBubbleView_cbv_cornerRadius)) {
            var cornerRadius = style.getDimension(R.styleable.ChatBubbleView_cbv_cornerRadius, 0F)
            if (cornerRadius < 0F) {
                cornerRadius = 0F
            }
            mCornerRadiusRect = RectF(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        } else {
            val cornerRadiusLeftTop = style.getDimension(R.styleable.ChatBubbleView_cbv_cornerRadiusLeftTop, 0F)
            val cornerRadiusLeftBottom = style.getDimension(R.styleable.ChatBubbleView_cbv_cornerRadiusLeftBottom, 0F)
            val cornerRadiusRightTop = style.getDimension(R.styleable.ChatBubbleView_cbv_cornerRadiusRightTop, 0F)
            val cornerRadiusRightBottom = style.getDimension(R.styleable.ChatBubbleView_cbv_cornerRadiusRightBottom, 0F)
            mCornerRadiusRect = RectF(cornerRadiusLeftTop, cornerRadiusLeftBottom, cornerRadiusRightTop, cornerRadiusRightBottom)
        }
        val borderColor = style.getColor(R.styleable.ChatBubbleView_cbv_borderColor, Color.TRANSPARENT)
        val borderWidth = style.getDimension(R.styleable.ChatBubbleView_cbv_borderWidth, 0F)
        mBorderWidth = borderWidth
        mPinDirection = style.getInt(R.styleable.ChatBubbleView_cbv_pinDirection, PIN_DIRECTION_LEFT)
        mPinWidth = style.getDimension(R.styleable.ChatBubbleView_cbv_pinWidth, 0F)
        mPinHeight = style.getDimension(R.styleable.ChatBubbleView_cbv_pinHeight, 0F)
        mPinMargin = style.getDimension(R.styleable.ChatBubbleView_cbv_pinMargin, 0F)
        mPinTipMargin = style.getDimension(R.styleable.ChatBubbleView_cbv_pinTipMargin, 0F)
        mPinTipRoundRadius = style.getDimension(R.styleable.ChatBubbleView_cbv_pinTipRoundRadius, 0F)
        mShadowColor = style.getColor(R.styleable.ChatBubbleView_cbv_shadowColor, Color.TRANSPARENT)
        mDrawShadow = mShadowColor != Color.TRANSPARENT
        val density = resources.displayMetrics.density
        mShadowDx = style.getDimension(R.styleable.ChatBubbleView_cbv_shadowDx, density * 2F)
        mShadowDy = style.getDimension(R.styleable.ChatBubbleView_cbv_shadowDy, density * 2F)
        style.recycle()

        mPaint = Paint().apply {
            color = borderColor
            this.style = Paint.Style.STROKE
            strokeWidth = borderWidth
            isAntiAlias = true
        }
        mShadowPaint = Paint().apply {
            isAntiAlias = true
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (canvas.isHardwareAccelerated && Build.VERSION.SDK_INT <= 16) {
            super.dispatchDraw(canvas)
        } else {
            if (mDrawShadow) {
                canvas.save()
                canvas.translate(mShadowDx, mShadowDy)
                canvas.drawPath(mShadowPath, mShadowPaint)
                canvas.restore()
            }
            canvas.save()
            canvas.clipPath(mClipPath)
            super.dispatchDraw(canvas)
            canvas.restore()
            canvas.drawPath(mClipPath, mPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        if (mDrawShadow) {
            setMeasuredDimension((measuredWidth + mShadowDx).toInt(), (measuredHeight + mShadowDy).toInt())
        }
        val pinWidth = mPinWidth //箭头宽度
        val pinHeight = mPinHeight //箭头高度
        val cornerRadiusRect = mCornerRadiusRect //圆角四个位置矩形
        val cornerRadiusLeftTop = cornerRadiusRect.left //左上边圆角半径
        val cornerRadiusLeftBottom = cornerRadiusRect.top //上下边圆角半径
        val cornerRadiusRightTop = cornerRadiusRect.right //右上边圆角半径
        val cornerRadiusRightBottom = cornerRadiusRect.bottom //下右边圆角半径
        val pinBorderWidthR = mBorderWidth / 2F //箭头边框等于气泡边宽/2
        val left = pinBorderWidthR
        val top = pinBorderWidthR
        val right = measuredWidth - pinBorderWidthR
        val bottom = measuredHeight - pinBorderWidthR
        var pinMargin = mPinMargin
        val pinTipMargin = mPinTipMargin
        val pinTipRoundRadius = mPinTipRoundRadius
        //重置路径
        val clipPath = mClipPath
        clipPath.reset()

        when (mPinDirection) {
            PIN_DIRECTION_LEFT -> {
                if (pinMargin < 0F) {
                    pinMargin = 0F
                } else {
                    val maxPinMargin = bottom - cornerRadiusLeftTop - cornerRadiusLeftBottom - pinHeight - pinBorderWidthR * 2F
                    if (pinMargin > maxPinMargin) {
                        pinMargin = maxPinMargin
                    }
                }
                val pinTipY = cornerRadiusLeftTop + pinMargin + top
                //移动到左上角
                clipPath.moveTo(left + pinWidth, cornerRadiusLeftTop) //b点坐标
                //绘制箭头与左上角圆角间距
                clipPath.lineTo(left + pinWidth, pinTipY) //画b到d的线
                //绘制箭头
                val pinTipMarinRange = pinHeight / 2F
                if (pinTipRoundRadius == 0F) {
                    //绘制箭头上半部分
                    clipPath.lineTo(left, pinTipY + pinTipMarinRange + pinTipMargin) //画从d到e点坐标的线
                    //绘制箭头下半部分
                    clipPath.lineTo(left + pinWidth, pinTipY + pinHeight) //画从e到f点坐标的线
                } else {
                    val pinTipRoundRadiusRect = mPinTipRoundRadiusRect
                    val pinTipCenterXR = left + pinWidth - pinTipRoundRadius
                    //计算箭头上半部分坐标
                    val pinTipCenterTopYR = pinTipMarinRange + pinTipMargin
                    val pinTipTopDistance = Math.sqrt(
                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterTopYR * pinTipCenterTopYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
                    val pinTipTopRadian = Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterTopYR).toDouble()) - Math.atan(
                            pinTipRoundRadius / pinTipTopDistance)
                    val pinTipTopRadianR = Math.PI / 2.0 - pinTipTopRadian
                    val pinTipTopXR = (Math.cos(pinTipTopRadian) * pinTipTopDistance).toFloat()
                    val pinTipTopYR = (Math.sin(pinTipTopRadian) * pinTipTopDistance).toFloat()
                    //绘制箭头上半部分
                    clipPath.lineTo(left + pinWidth - pinTipTopXR + left, pinTipY + pinTipTopYR)
                    //计算箭头下半部分坐标
                    val pinTipCenterBottomYR = pinTipMarinRange - pinTipMargin
                    val pinTipBottomDistance = Math.sqrt(
                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterBottomYR * pinTipCenterBottomYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
                    val pinTipBottomRadianR = Math.PI / 2.0 - (Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterBottomYR).toDouble()) - Math.atan(
                            pinTipRoundRadius / pinTipBottomDistance))
                    //绘制箭头圆角
                    pinTipRoundRadiusRect.set(left, pinTipY + pinTipMargin - pinTipRoundRadius + pinHeight / 2F, pinTipRoundRadius * 2F,
                            pinTipY + pinTipMargin + pinTipRoundRadius + pinHeight / 2F)
                    clipPath.arcTo(pinTipRoundRadiusRect, Math.toDegrees(pinTipTopRadianR).toFloat() - 180F,
                            Math.toDegrees(-pinTipTopRadianR - pinTipBottomRadianR).toFloat(), false)
                    //绘制箭头下半部分
                    clipPath.lineTo(left + pinWidth, pinTipY + pinHeight)
                }
                //向下绘制至左下角圆角处
                clipPath.lineTo(left + pinWidth, bottom - cornerRadiusLeftBottom) //绘制从f到g点坐标的直线
                //绘制左下角圆角
                clipPath.quadTo(left + pinWidth, bottom, left + pinWidth + cornerRadiusLeftBottom, bottom) //画g到i的贝塞尔曲线
                //向右绘制至右下角圆角处
                clipPath.lineTo(right - cornerRadiusRightBottom, bottom) //画从i到j的直线
                //绘制右下角圆角
                clipPath.quadTo(right, bottom, right, bottom - cornerRadiusRightBottom) //画从j到l的贝塞尔曲线
                //向上绘制至右上角圆角处
                clipPath.lineTo(right, cornerRadiusRightTop) //画从l到m的直线
                //绘制右上角圆角
                clipPath.quadTo(right, top, right - cornerRadiusRightTop, top) //画从m到o的贝塞尔曲线
                //向左绘制至左上角圆角处
                clipPath.lineTo(left + pinWidth + cornerRadiusLeftTop, top) //画从o到c的直线
                //绘制左上角圆角
                clipPath.quadTo(left + pinWidth, top, left + pinWidth, top + cornerRadiusLeftTop) //
            }
            PIN_DIRECTION_TOP -> {
                if (pinMargin < 0F) {
                    pinMargin = 0F
                } else {
                    val maxPinMargin = right - cornerRadiusLeftTop - cornerRadiusRightTop - pinHeight - pinBorderWidthR * 2F
                    if (pinMargin > maxPinMargin) {
                        pinMargin = maxPinMargin
                    }
                }
                val pinTipX = cornerRadiusLeftTop + pinMargin + left
                //移动到左上角
                // clipPath.moveTo(left + pinWidth, cornerRadiusLeftTop)
                clipPath.moveTo(left + cornerRadiusLeftTop, top + pinWidth) //移到a点坐标
                //绘制箭头与左上角圆角间距
                clipPath.lineTo(pinTipX, top + pinWidth) //画a到b的直线
                //绘制箭头
                val pinTipMarinRange = pinHeight / 2F
                if (pinTipRoundRadius == 0F) {
                    //绘制箭头上半部分
                    clipPath.lineTo(pinTipX + pinTipMarinRange + pinTipMargin, top) //画b到c的直线
                    //绘制箭头下半部分
                    clipPath.lineTo(pinTipX + pinHeight, top + pinWidth) //从c到d的直线
                } else {
//                    val pinTipRoundRadiusRect = mPinTipRoundRadiusRect
//                    val pinTipCenterXR = left + pinWidth - pinTipRoundRadius
//                    //计算箭头上半部分坐标
//                    val pinTipCenterTopYR = pinTipMarinRange + pinTipMargin
//                    val pinTipTopDistance = Math.sqrt(
//                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterTopYR * pinTipCenterTopYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
//                    val pinTipTopRadian = Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterTopYR).toDouble()) - Math.atan(
//                            pinTipRoundRadius / pinTipTopDistance)
//                    val pinTipTopRadianR = Math.PI / 2.0 - pinTipTopRadian
//                    val pinTipTopXR = (Math.cos(pinTipTopRadian) * pinTipTopDistance).toFloat()
//                    val pinTipTopYR = (Math.sin(pinTipTopRadian) * pinTipTopDistance).toFloat()
//                    //绘制箭头上半部分
//                    clipPath.lineTo(left + pinWidth - pinTipTopXR + left, pinTipX + pinTipTopYR)
//                    //计算箭头下半部分坐标
//                    val pinTipCenterBottomYR = pinTipMarinRange - pinTipMargin
//                    val pinTipBottomDistance = Math.sqrt(
//                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterBottomYR * pinTipCenterBottomYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
//                    val pinTipBottomRadianR = Math.PI / 2.0 - (Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterBottomYR).toDouble()) - Math.atan(
//                            pinTipRoundRadius / pinTipBottomDistance))
//                    //绘制箭头圆角
//                    pinTipRoundRadiusRect.set(left, pinTipX + pinTipMargin - pinTipRoundRadius + pinHeight / 2F, pinTipRoundRadius * 2F,
//                            pinTipX + pinTipMargin + pinTipRoundRadius + pinHeight / 2F)
//                    clipPath.arcTo(pinTipRoundRadiusRect, Math.toDegrees(pinTipTopRadianR).toFloat() - 180F,
//                            Math.toDegrees(-pinTipTopRadianR - pinTipBottomRadianR).toFloat(), false)
//                    //绘制箭头下半部分
//                    clipPath.lineTo(left + pinWidth, pinTipX + pinHeight)
                }
                //向下绘制至左下角圆角处
                clipPath.lineTo(right - cornerRadiusRightTop, top + pinWidth) //画从d到e的直线
                //绘制右上角圆角
                clipPath.quadTo(right, top + pinWidth, right, top + pinWidth + cornerRadiusRightTop) //画e到g的贝塞尔曲线
                //向右绘制至右下角圆角处
                clipPath.lineTo(right, bottom - cornerRadiusRightBottom) //画g到h的直线
                //绘制右下角圆角
                clipPath.quadTo(right, bottom, right - cornerRadiusRightBottom, bottom) //画h到j的贝塞尔曲线
                //向左绘制至左下角圆角处
                clipPath.lineTo(left + cornerRadiusLeftBottom, bottom) //画j到k的直线
                //绘制左下角圆角
                clipPath.quadTo(left, bottom, left, bottom - cornerRadiusLeftBottom) //画从k到m的贝塞尔曲线
                //向上绘制至左上角圆角处
                clipPath.lineTo(left, top + pinWidth + cornerRadiusLeftTop) //画m到n的直线
                //绘制左上角圆角
                clipPath.quadTo(left, top + pinWidth, left + cornerRadiusLeftTop, top + pinWidth) //画n到a的贝塞尔曲线
            }
            PIN_DIRECTION_RIGHT -> {
                if (pinMargin < 0F) {
                    pinMargin = 0F
                } else {
                    val maxPinMargin = bottom - cornerRadiusLeftTop - cornerRadiusLeftBottom - pinHeight - pinBorderWidthR * 2F
                    if (pinMargin > maxPinMargin) {
                        pinMargin = maxPinMargin
                    }
                }
                val pinTipY = cornerRadiusRightTop + pinMargin + top
                //移动到右上角
                clipPath.moveTo(right - pinWidth, cornerRadiusRightTop)
                //绘制箭头与右上角圆角间距
                clipPath.lineTo(right - pinWidth, pinTipY)
                //绘制箭头
                val pinTipMarinRange = pinHeight / 2F
                if (pinTipRoundRadius == 0F) {
                    //绘制箭头上半部分
                    clipPath.lineTo(right, pinTipY + pinTipMarinRange + pinTipMargin)
                    //绘制箭头下半部分
                    clipPath.lineTo(right - pinWidth, pinTipY + pinHeight)
                } else {
                    val pinTipRoundRadiusRect = mPinTipRoundRadiusRect
                    val pinTipCenterXR = pinWidth - pinTipRoundRadius
                    //计算箭头上半部分坐标
                    val pinTipCenterTopYR = pinTipMarinRange + pinTipMargin
                    val pinTipTopDistance = Math.sqrt(
                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterTopYR * pinTipCenterTopYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
                    val pinTipTopRadian = Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterTopYR).toDouble()) - Math.atan(
                            pinTipRoundRadius / pinTipTopDistance)
                    val pinTipTopRadianR = Math.PI / 2.0 - pinTipTopRadian
                    val pinTipTopXR = (Math.cos(pinTipTopRadian) * pinTipTopDistance).toFloat()
                    val pinTipTopYR = (Math.sin(pinTipTopRadian) * pinTipTopDistance).toFloat()
                    //绘制箭头上半部分
                    clipPath.lineTo(right - (pinWidth - pinTipTopXR), pinTipY + pinTipTopYR)
                    //计算箭头下半部分坐标
                    val pinTipCenterBottomYR = pinTipMarinRange - pinTipMargin
                    val pinTipBottomDistance = Math.sqrt(
                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterBottomYR * pinTipCenterBottomYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
                    val pinTipBottomRadianR = Math.PI / 2.0 - (Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterBottomYR).toDouble()) - Math.atan(
                            pinTipRoundRadius / pinTipBottomDistance))
                    //绘制箭头圆角
                    pinTipRoundRadiusRect.set(right - pinTipRoundRadius * 2F, pinTipY + pinTipMargin - pinTipRoundRadius + pinHeight / 2F, right,
                            pinTipY + pinTipMargin + pinTipRoundRadius + pinHeight / 2F)
                    clipPath.arcTo(pinTipRoundRadiusRect, Math.toDegrees(pinTipTopRadianR).toFloat() + 180F,
                            -Math.toDegrees(-pinTipTopRadianR - pinTipBottomRadianR).toFloat(), false)
                    //绘制箭头下半部分
                    clipPath.lineTo(right - pinWidth, pinTipY + pinHeight)
                }
                //向下绘制至右下角圆角处
                clipPath.lineTo(right - pinWidth, bottom - cornerRadiusRightBottom)
                //绘制右下角圆角
                clipPath.quadTo(right - pinWidth, bottom, right - (pinWidth + cornerRadiusRightBottom), bottom)
                //向左绘制至左下角圆角处
                clipPath.lineTo(left + cornerRadiusLeftBottom, bottom)
                //绘制左下角圆角
                clipPath.quadTo(left, bottom, left, bottom - cornerRadiusLeftBottom)
                //向上绘制至左上角圆角处
                clipPath.lineTo(left, cornerRadiusLeftTop)
                //绘制左上角圆角
                clipPath.quadTo(left, top, left + cornerRadiusLeftTop, top)
                //向右绘制至右上角圆角处
                clipPath.lineTo(right - (pinWidth + cornerRadiusRightTop), top)
                //绘制右上角圆角
                clipPath.quadTo(right - pinWidth, top, right - pinWidth, top + cornerRadiusRightTop)
            }
            PIN_DIRECTION_BOTTOM -> {
                if (pinMargin < 0F) {
                    pinMargin = 0F
                } else {
                    val maxPinMargin = right - cornerRadiusLeftTop - cornerRadiusRightTop - pinHeight - pinBorderWidthR * 2F
                    if (pinMargin > maxPinMargin) {
                        pinMargin = maxPinMargin
                    }
                }
                val pinTipX = cornerRadiusLeftBottom + pinMargin + left
                //移动到左下角
                clipPath.moveTo(left + cornerRadiusLeftBottom, bottom - pinWidth) //移到a点坐标
                //绘制箭头与左上角圆角间距
                clipPath.lineTo(pinTipX, bottom - pinWidth) //画a到b的直线
                //绘制箭头
                val pinTipMarinRange = pinHeight / 2F
                if (pinTipRoundRadius == 0F) {
                    //绘制箭头上半部分
                    clipPath.lineTo(pinTipX + pinTipMarinRange + pinTipMargin, bottom) //画b到c的直线
                    //绘制箭头下半部分
                    clipPath.lineTo(pinTipX + pinHeight, bottom - pinWidth) //从c到d的直线
                } else {
                    //                    val pinTipRoundRadiusRect = mPinTipRoundRadiusRect
                    //                    val pinTipCenterXR = left + pinWidth - pinTipRoundRadius
                    //                    //计算箭头上半部分坐标
                    //                    val pinTipCenterTopYR = pinTipMarinRange + pinTipMargin
                    //                    val pinTipTopDistance = Math.sqrt(
                    //                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterTopYR * pinTipCenterTopYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
                    //                    val pinTipTopRadian = Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterTopYR).toDouble()) - Math.atan(
                    //                            pinTipRoundRadius / pinTipTopDistance)
                    //                    val pinTipTopRadianR = Math.PI / 2.0 - pinTipTopRadian
                    //                    val pinTipTopXR = (Math.cos(pinTipTopRadian) * pinTipTopDistance).toFloat()
                    //                    val pinTipTopYR = (Math.sin(pinTipTopRadian) * pinTipTopDistance).toFloat()
                    //                    //绘制箭头上半部分
                    //                    clipPath.lineTo(left + pinWidth - pinTipTopXR + left, pinTipY + pinTipTopYR)
                    //                    //计算箭头下半部分坐标
                    //                    val pinTipCenterBottomYR = pinTipMarinRange - pinTipMargin
                    //                    val pinTipBottomDistance = Math.sqrt(
                    //                            (pinTipCenterXR * pinTipCenterXR + pinTipCenterBottomYR * pinTipCenterBottomYR - pinTipRoundRadius * pinTipRoundRadius).toDouble())
                    //                    val pinTipBottomRadianR = Math.PI / 2.0 - (Math.PI / 2.0 - Math.atan((pinTipCenterXR / pinTipCenterBottomYR).toDouble()) - Math.atan(
                    //                            pinTipRoundRadius / pinTipBottomDistance))
                    //                    //绘制箭头圆角
                    //                    pinTipRoundRadiusRect.set(left, pinTipY + pinTipMargin - pinTipRoundRadius + pinHeight / 2F, pinTipRoundRadius * 2F,
                    //                            pinTipY + pinTipMargin + pinTipRoundRadius + pinHeight / 2F)
                    //                    clipPath.arcTo(pinTipRoundRadiusRect, Math.toDegrees(pinTipTopRadianR).toFloat() - 180F,
                    //                            Math.toDegrees(-pinTipTopRadianR - pinTipBottomRadianR).toFloat(), false)
                    //                    //绘制箭头下半部分
                    //                    clipPath.lineTo(left + pinWidth, pinTipY + pinHeight)
                }
                //向下绘制至左下角圆角处
                clipPath.lineTo(right - cornerRadiusRightBottom, bottom - pinWidth) //画从d到e的直线
                //绘制右上角圆角
                clipPath.quadTo(right, bottom - pinWidth, right, bottom - pinWidth - cornerRadiusRightBottom) //画e到g的贝塞尔曲线
                //向右绘制至右下角圆角处
                clipPath.lineTo(right, top + cornerRadiusRightTop) //画g到h的直线
                //绘制右下角圆角
                clipPath.quadTo(right, top, right - cornerRadiusRightTop, top) //画h到j的贝塞尔曲线
                //向左绘制至左下角圆角处
                clipPath.lineTo(left + cornerRadiusLeftTop, top) //画j到k的直线
                //绘制左下角圆角
                clipPath.quadTo(left, top, left, top + cornerRadiusLeftTop) //画从k到N的贝塞尔曲线
                //向上绘制至左上角圆角处
                clipPath.lineTo(left, bottom - pinWidth - cornerRadiusLeftBottom) //画n到o的直线
                //绘制左上角圆角
                clipPath.quadTo(left, bottom - pinWidth, left + cornerRadiusLeftBottom, bottom - pinWidth) //画n到a的贝塞尔曲线
            }
        }
        //闭合路径
        clipPath.close()
        if (mDrawShadow) {
            mShadowPath.set(clipPath)
            mShadowPaint.shader = RadialGradient(measuredWidth / 2F, measuredHeight / 2F,
                    Math.sqrt((measuredWidth * measuredWidth + measuredHeight * measuredHeight).toDouble()).toFloat(), mShadowColor, Color.TRANSPARENT,
                    Shader.TileMode.CLAMP)
        }
    }
}