/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package org.xclcharts.chart;

import java.util.List;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.common.MathHelper;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.CirChart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

/**
 * @ClassName PieChart
 * @Description  饼图基类
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */
public class PieChart extends CirChart{
	
	private static final String TAG = "PieChart";
			
	//是否使用渲染来突出效果
	private boolean mGradient = true;
	//选中区偏移长度
	protected static final float SELECTED_OFFSET = 10.0f;

	//数据源
	private List<PieData> mDataset;
	
	protected RectF mRectF = null;

	public PieChart()
	{
		super();
	}


	/**
	 * 设置图表的数据源
	 * @param piedata 来源数据集
	 */
	public void setDataSource(List<PieData> piedata)
	{
		if(null != mDataset)mDataset.clear();
		this.mDataset = piedata; 
	}
	
	/**
	 * 返回数据轴的数据源
	 * @return 数据源
	 */
	public List<PieData> getDataSource()
	{
		return mDataset;
	}
	
	/**
	 * 显示渲染效果(此函数对3D饼图无效)
	 */
	public void showGradient()
	{
		mGradient = true;
	}
	
	/**
	 * 隐藏渲染效果(此函数对3D饼图无效)
	 */
	public void hideGradient()
	{
		mGradient = false;
	}
	
	/**
	 * 确认是否可使用渲染效果(此函数对3D饼图无效)
	 * @return 是否使用渲染
	 */
	public boolean getGradient()
	{
		return mGradient;
	}
	  
	/**
	 * 给画笔设置渲染颜色和效果
	 * @param paintArc	画笔
	 * @param cirX		中心点X坐标
	 * @param cirY		中心点Y坐标
	 * @param radius	半径
	 * @return	返回渲染效果类
	 */
	private RadialGradient renderRadialGradient(Paint paintArc,
												final float cirX,
												final float cirY,
												final float radius)
	{				
		float radialRadius = (float) (radius * 0.8f);				
		int color = paintArc.getColor();		
		int darkerColor = DrawHelper.getInstance().getDarkerColor(color);
	
		RadialGradient radialGradient = new RadialGradient(cirX, cirY, radialRadius,
				darkerColor,color,
				Shader.TileMode.MIRROR); 
		
		//返回环形渐变  
		return radialGradient;
	}
	
	/**
	 * 检查角度的合法性
	 * @param Angle 角度
	 * @return 是否正常
	 */
	private boolean validateAngle(float Angle)
	{
		if(Float.compare(Angle, 0.0f) == 0 
				|| Float.compare(Angle, 0.0f) == -1)
		{
			Log.e(TAG, "扇区圆心角小于等于0度. 当前圆心角为:"+Float.toString(Angle));
			return false;
		}
		return true;
	}
		
	/**
	 * 绘制指定角度扇区
	 * @param paintArc 画笔
	 * @param arcRF0   范围
	 * @param cData  数据集
	 * @param cirX   中心点X坐标
	 * @param cirY   中心点Y坐标
	 * @param radius  半径
	 * @param offsetAngle 偏移角度
	 * @param curretAngle 当前绘制角度
	 * @throws Exception  例外处理
	 */
	protected boolean drawSlice(Canvas canvas, Paint paintArc,RectF arcRF0,
							PieData cData,
							final float cirX,
							final float cirY,
							final float radius,
							final float offsetAngle,
							final float curretAngle) throws Exception
	{
		try{
			if(!validateAngle(curretAngle))return false;
			
			// 绘制环形渐变
			if(getGradient())
				paintArc.setShader(renderRadialGradient(paintArc,cirX,cirY,radius));
	        
			//在饼图中显示所占比例  
        	canvas.drawArc(arcRF0, offsetAngle, curretAngle, true, paintArc);
         
            //标签 
        	renderLabel(canvas,cData.getLabel(),cirX, cirY,
	        			radius,offsetAngle,curretAngle);  
        	return true;
		}catch( Exception e){
			throw e;
		}
	}
	
	protected void initRectF(float left,float top,float right,float bottom)
	{
		if(null == mRectF)
        {
        	mRectF = new RectF(left ,top,right,bottom);
        }else{
        	mRectF.left = left;
        	mRectF.top = top; 
        	mRectF.right = right;
        	mRectF.bottom = bottom;
        }
	}
		
	
	/**
	 * 绘制指定角度扇区
	 * @param paintArc 画笔
	 * @param cData 数据集
	 * @param cirX  中心点X坐标
	 * @param cirY  中心点Y坐标
	 * @param radius 半径
	 * @param offsetAngle 偏移角度
	 * @param curretAngle 当前绘制角度
	 * @throws Exception  例外处理
	 */
	protected boolean drawSelectedSlice(Canvas canvas, Paint paintArc,
									PieData cData,
									final float cirX,
									final float cirY,
									final float radius,
									final float offsetAngle,
									final float curretAngle) throws Exception
	{
		try{			
			if(!validateAngle(curretAngle))return false;
			
			//偏移圆心点位置(默认偏移半径的1/10)
	    	float newRadius = div(radius , SELECTED_OFFSET);
	    	 //计算百分比标签
	    	MathHelper.getInstance().calcArcEndPointXY(cirX,cirY,
	    							newRadius,
	    							add(offsetAngle , curretAngle/2f)); 	
	        
	        float arcLeft = sub(MathHelper.getInstance().getPosX() , radius);  
	        float arcTop  = sub(MathHelper.getInstance().getPosY() , radius); 
	        float arcRight = add(MathHelper.getInstance().getPosX() , radius); 
	        float arcBottom = add(MathHelper.getInstance().getPosY() , radius); 
	        initRectF(arcLeft ,arcTop,arcRight,arcBottom);   	        
	        
	        //绘制环形渐变  
	        if(getGradient())
	        	paintArc.setShader(renderRadialGradient(paintArc,cirX,cirY,radius));
	        
	        //在饼图中显示所占比例  
	        canvas.drawArc(mRectF, offsetAngle, curretAngle, true, paintArc);
	        	        
	        //标签
	        renderLabel(canvas,cData.getLabel(),
			        			MathHelper.getInstance().getPosX(), 
			        			MathHelper.getInstance().getPosY(),
			        			radius,offsetAngle,curretAngle);	
	        
	        return true;	        
		}catch( Exception e){
			 throw e;
		}
	}
	
	/**
	 * 绘制图
	 */
	protected boolean renderPlot(Canvas canvas)
	{
	
		try{				
			if(null == mDataset)
			{
	 			Log.e(TAG,"数据源为空.");
	 			return false;
			}
			//中心点坐标
			float cirX = plotArea.getCenterX();
		    float cirY = plotArea.getCenterY();		     
	        float radius = getRadius();
	              
	        //确定饼图范围
	        float arcLeft = sub(cirX , radius);  
	        float arcTop  = sub(cirY , radius) ;  
	        float arcRight = add(cirX , radius) ;  
	        float arcBottom = add(cirY , radius) ;  
	        RectF arcRF0 = new RectF(arcLeft ,arcTop,arcRight,arcBottom);   	        	     
		     
	        //画笔初始化
			Paint paintArc = new Paint();  
			paintArc.setAntiAlias(true);	
			
			//用于存放当前百分比的圆心角度
			float currentAngle = 0.0f;		
			int i = 0;
			float offsetAngle = mOffsetAngle;
			
			for(PieData cData : mDataset)
			{
				paintArc.setColor(cData.getSliceColor());				
				currentAngle = cData.getSliceAngle();		
				if(Float.compare(currentAngle,0.0f) == 0 
						|| Float.compare(currentAngle,0.0f) == -1 )continue;				
								
			    if(cData.getSelected()) //指定突出哪个块
	            {			    	            		            	
	            	if(!drawSelectedSlice(canvas,paintArc,cData,
	            			cirX,cirY,radius,
	            			offsetAngle,currentAngle))return false;			    		            		            		            
	            }else{
	            	if(!drawSlice(canvas,paintArc,arcRF0,cData,
	            			cirX,cirY,radius,
	            			offsetAngle,(float) currentAngle))return false;	            	
	            }
			    
			    //保存角度
			    saveArcRecord(i,cirX,cirY,radius,offsetAngle,currentAngle);
			    
	          //下次的起始角度  
			    offsetAngle = add(offsetAngle, currentAngle);
	            i++;
			}					
			
			//图KEY
			plotLegend.renderPieKey(canvas,this.mDataset);
		
		 }catch( Exception e){
			 Log.e(TAG,e.toString());
			 return false;
		 }
		 return true;
	}
	
	/**
	 * 检验传入参数,累加不能超过360度
	 * @return 是否通过效验
	 */
	protected boolean validateParams()
	{		
		if(null == mDataset)return false;
		float totalAngle = 0.0f;	
				
		for(PieData cData : mDataset)
		{			
			float currentValue = cData.getSliceAngle();			
			totalAngle = add(totalAngle,currentValue);					
			if( Float.compare(totalAngle,0.0f) == -1)
			{
				Log.e(TAG,"传入参数不合理，圆心角总计小于等于0度. 现有圆心角合计:"
						+Float.toString(totalAngle)
						+" 当前圆心角:"+Float.toString( currentValue )
						+" 当前百分比:"+Double.toString( cData.getPercentage() ));
				return false;
			}else if( Float.compare(totalAngle, 360f) == 1) 
			{
				//圆心角总计大于360度
				Log.e(TAG,"传入参数不合理，圆心角总计大于360度. 现有圆心角合计:"
							+Float.toString(totalAngle));
				return false;
			}
		}				
		return true;
	}

	/**
	 * 返回当前点击点的信息
	 * @param x 点击点X坐标
	 * @param y	点击点Y坐标
	 * @return 返回对应的位置记录
	 */
	public ArcPosition getPositionRecord(float x,float y)
	{		
		return getArcRecord(x,y);
	}		
	
	@Override
	protected boolean postRender(Canvas canvas) throws Exception 
	{	
		try {
			super.postRender(canvas);
			
			//检查值是否合理
	        if(false == validateParams())return false;
			
			//绘制图表
			return renderPlot(canvas);
		} catch (Exception e) {
			throw e;
		}
	}
	
}
