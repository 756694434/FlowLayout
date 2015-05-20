package com.example.flowlayout;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyFlowLayout extends ViewGroup{

	HashMap<Integer, Integer> mLineViewMap;
	HashMap<Integer, Integer> mLineHeightMap;
	int mLineCount;

	public MyFlowLayout(Context context) {
		this(context,null);
	}

	public MyFlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MyFlowLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected LayoutParams generateLayoutParams(LayoutParams p) {
		return new MarginLayoutParams(p);
	}


	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ������ĸ�����Ϊ�����õĲ���ģʽ�ʹ�С
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// �����warp_content����£���¼��͸�  
		int width = 0;  
		int height = 0;  

		//�ۼ�
		int lineWidth = 0;
		int lineHeight = 0;		


		int lineNum = 0;
		int cCount =  getChildCount();
		mLineViewMap =new HashMap<Integer, Integer>();
		mLineHeightMap = new HashMap<Integer, Integer>();
		for(int i=0;i<cCount;i++){
			View view = getChildAt(i);
			
			 if (view.getVisibility() == View.GONE)  
             {  
                 continue;  
             }  
			
			measureChild(view, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp =  (MarginLayoutParams)view.getLayoutParams();
			int viewHeight = lp.topMargin+lp.bottomMargin+view.getMeasuredHeight();  //measureChild����child�����child.getMeasuredHeight
			int viewWidth = lp.leftMargin +lp.rightMargin + view.getMeasuredWidth();

			if(lineWidth+viewWidth>sizeWidth-getPaddingLeft()-getPaddingRight()){    //Ҫ������
				mLineHeightMap.put(lineNum, lineHeight);

				height+=lineHeight;                   //�и��ۼ�
				width = Math.max(width, lineWidth);   //�����Ќ���Ϊviewgroup�Č�
				lineNum ++;
				lineWidth=0;
				lineHeight=0;
			}		
			lineWidth+=viewWidth;
			lineHeight = Math.max(lineHeight, viewHeight);  //����ߵ�childHeight��Ϊviewgroup��height



			if(i==cCount-1){   //���һ������ʱҲҪ����һ��
				height+=lineHeight;   
				width = Math.max(width, lineWidth); 
			}	
			mLineViewMap.put(i, lineNum);   //���Ǵ�0��ʼ����
		}


		//EXACTLY :  match_parent  ���߾���ֵ
		//AT_MOST :  wrap_content(��Ҫ�Լ����㌈��)

		setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth     
				: width+getPaddingLeft()+getPaddingRight(), (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight  
						: height+getPaddingTop()+getPaddingBottom());  

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int nowLineNum = 0;


		int nowLineWidth = getPaddingLeft();
		int nowLineHeight = getPaddingTop();

		int left=0;
		int right=0;
		int top=0;
		int bottom=0;


		int cCount =  getChildCount();
		for(int i=0;i<cCount;i++){  
			View child = getChildAt(i);  
			
			 if (child.getVisibility() == View.GONE)  
             {  
                 continue;  
             }  
			
			MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams(); 
			int childWidth = child.getMeasuredWidth();  
			int childHeight = child.getMeasuredHeight();  

			int viewWidth =  child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
			int viewHeight = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;
			int lineNum =  mLineViewMap.get(i);   //ȡ����view������

			if(nowLineNum!=lineNum){      //����	
				nowLineWidth=getPaddingLeft();  //��ʼ�� �ۼӌ�ֵ
				nowLineHeight+=mLineHeightMap.get(lineNum-1);
				nowLineNum++;	
			}

			left = nowLineWidth+lp.leftMargin;
			right = left+childWidth;
			top = nowLineHeight + lp.topMargin;
			bottom = top +childHeight;

			nowLineWidth+=viewWidth;

			child.layout(left, top, right, bottom);  
		}
	}
}
