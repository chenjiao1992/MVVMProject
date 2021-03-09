package com.cj.mvvmproject.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/****
 * 流式布局
 */
public class FlowLayout extends ViewGroup {

	// 定义水平方向的内间距
	private int horizontalSpcing = 15;
	// 定义垂直方向的内间距
	private int verticalSpcing = 15;
	// 定义集合用于存放数据
	private ArrayList<Line> lineList = new ArrayList<Line>();

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowLayout(Context context) {
		super(context);
	}

	// 设置水平间距
	public void setHorizontalSpcing(int horizontalSpcing) {
		if (horizontalSpcing > 0) {
			this.horizontalSpcing = horizontalSpcing;
		}
	}

	// 设置垂直间距
	public void setVerticalSpcing(int verticalSpcing) {
		if (verticalSpcing > 0) {
			this.verticalSpcing = verticalSpcing;
		}
	}

	/*****
	 * 重写方法.onMeasure() ---分行处理(类似于排座位表)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 计算当前的视图ViewGroup需要的宽度和高度值
		// 1.获取FlowLayout的宽度(ViewGroup的宽度:包含了padding)
		int width = MeasureSpec.getSize(widthMeasureSpec);
		// 2.得到实际用于比较的宽度.就是出去左右两边的宽度
		int noPaddingWidth = width - getPaddingLeft() - getPaddingLeft();
		// 准备Line的对象
		Line line = new Line();
		// 3.依次比较PK.遍历所有的子View.拿所有的子View和noPaddingWidth进行比较
		for (int i = 0; i < getChildCount(); i++) {
			// 得到当前的每一个孩子视图
			View childView = getChildAt(i);
			// 保证能够获取到值
			childView.measure(0, 0);
			// 4.如果在当前的line当中.一个子view都没有的情况.
			// 直接把第一个子View加载到第一行.因为要保证每一行至少有一个子View
			if (line.getViewList().size() == 0) {
				// 直接存放在当前的这一行
				line.addLineView(childView);
			} else if (line.getLineWidth() + horizontalSpcing
					+ childView.getMeasuredWidth() > noPaddingWidth) {
				// 5.如果当前的line的宽度+水平间距+此时此刻的View。大于了noPaddingWidth,则child需要换行
				// 把之前的line对象,先存入到
				lineList.add(line);
				// 创建新的行
				line = new Line();
				// 添加子View在当前的行当中
				line.addLineView(childView);
			} else {
				// 6.如果当前的行的第一项.而且在没有操作当前行.那么添加在当前的行
				line.addLineView(childView);
			}
			// 7.如果当前的child是最后的子View.那么需要保存最后的line对象
			if (i == (getChildCount() - 1)) {
				// 保存最后的一个line
				lineList.add(line);
			}
		}
		// for循环结束了.lineList存放了所有的Line.而每个Line又记录了自己行所有的自己行的View
		// 计算FlowLayout需要的高度
		// 先计算当前整个ViewGroup的上间距和下间距
		int height = getPaddingTop() + getPaddingBottom();
		// 遍历所有的行
		for (int i = 0; i < lineList.size(); i++) {
			// 加上所有的行高
			height += lineList.get(i).getLineHeight();
		}
		// 加上行间距
		height += (lineList.size() - 1) * verticalSpcing;
		// 设置当前的控件宽高.向父亲View申请的宽高
		setMeasuredDimension(width, height);
	}

	/***
	 * 去摆放所有的子View.让每个人真正做到自己的位置上
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// 得到当前的左边内间距和上面内间距
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();

		// 循环遍历所有的line
		for (int i = 0; i < lineList.size(); i++) {
			// 得到当前的行对象
			Line line = lineList.get(i);
			// 得到当前的子集合
			ArrayList<View> viewList = line.getViewList();
			// ==============[去掉留白的操作]================
			// 得到当前的留白区间大小
			int remainSpacing = getMeasuredWidth() - getPaddingLeft()
					- getPaddingRight() - line.getLineWidth();
			// 计算每个View平均得到的值
			float perSpacing = remainSpacing / viewList.size();
			// ==========================================

			// 从第二行开始.每一行都比上一行多一个行高和垂直间距
			if (i > 0) {
				paddingTop += (verticalSpcing + lineList.get(i - 1)
						.getLineHeight());
			}

			// 循环遍历当前的集合
			for (int j = 0; j < viewList.size(); j++) {
				// 得到每一个子View
				View childView = viewList.get(j);

				// 将得到的添加到子View的宽度上面
				int widthSpac = MeasureSpec.makeMeasureSpec(
						(int) (childView.getMeasuredWidth() + perSpacing),
						MeasureSpec.EXACTLY);
				// 设置当前的宽度
				childView.measure(widthSpac, 0);
				// 摆放的过程
				// 判断是不是每一行的第一个
				if (j == 0) {
					childView.layout(paddingLeft, paddingTop, paddingLeft
							+ childView.getMeasuredWidth(), paddingTop
							+ childView.getMeasuredHeight());

				} else {
					// 如果不是每一行的第一个,需要参考前一个View的right
					View preView = viewList.get(j - 1);
					// 当前view的left是前一个view的right+水平间距
					int left = preView.getRight() + horizontalSpcing;
					// 当前的
					childView.layout(left, preView.getTop(),
							left + childView.getMeasuredWidth(),
							preView.getBottom());
				}
			}

		}
	}

	/***
	 * 封装类 line 用于表示一行的数据.包括所有的子View.每行高和每行宽
	 */
	class Line {
		// 每一行当前需要存放的子View的集合
		private ArrayList<View> viewList = new ArrayList<View>();
		private int width;
		private int height;

		/***
		 * 提供一个专门用于存在在Line行的操作
		 * 
		 * @return
		 */
		public void addLineView(View childView) {
			// 判断当前的行数当值是否存在子View
			if (!viewList.contains(childView)) {
				// 添加子View
				viewList.add(childView);
				// 如果当前加载完毕之后
				if (viewList.size() == 1) {
					// 说明添加的是第一个字View.那么line的宽度就是字view的宽度
					width = childView.getMeasuredWidth();
				} else {
					// 如果加载的不是第一个.那么需要加上原始的宽度和水平间距
					width += (childView.getMeasuredWidth() + horizontalSpcing);
				}
				// 更新line的height(如果所有的高度当中,遇到一个较高的.那么最终的高度就是较高的)
				height = Math.max(childView.getMeasuredHeight(), height);
			}

		}

		// 获取当前的行宽度
		public int getLineWidth() {
			return width;
		}

		// 获取当前的行高度
		public int getLineHeight() {
			return height;
		}

		// 获取当前行的所有子View
		public ArrayList<View> getViewList() {
			return viewList;
		}
	}

}
