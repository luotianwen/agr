package com.zte.agricul.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;

import com.zte.agricul.zh.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class SplineChart03View extends TouchView {

	private String TAG = "SplineChart03View";
	private SplineChart chart = new SplineChart();
	// �������ǩ����
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
	LinkedHashMap<Double, Double> linePoint1 = new LinkedHashMap<Double, Double>();
	LinkedHashMap<Double, Double> linePoint2 = new LinkedHashMap<Double, Double>();
	public SplineChart03View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// initView();
	}

	public SplineChart03View(Context context, AttributeSet attrs) {
		super(context, attrs);
		// initView();
	}

	public SplineChart03View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// initView();
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();
	}

	public void initData() {
		chartRender();
	}
	public SplineChart getSplineChart() {
		return chart;
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// ͼ��ռ��Χ��С
		chart.setChartRange(w, h);
	}

	private void chartRender() {
		try {

			// ���û�ͼ��Ĭ������pxֵ,���ÿռ���ʾAxis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

			// ��ʾ�߿�
			// chart.showRoundBorder();

			// ����Դ
			chart.setCategories(labels);
			chart.setDataSource(chartData);

			// ����ͼ�ı���ɫ
			chart.setApplyBackgroundColor(true);
			chart.setBackgroundColor( (int)Color.rgb(173,216,230) );
			chart.getBorder().setBorderLineColor((int)Color.rgb(179, 147, 197));
			
			
			// �������������߷��
			chart.getCategoryAxis().setTickMarksVisible(false);
			chart.getDataAxis().setAxisLineVisible(false);
			chart.getDataAxis().setTickMarksVisible(false);
			chart.getPlotGrid().showHorizontalLines();
			chart.setTopAxisVisible(false);
			chart.setRightAxisVisible(false);

			chart.getPlotGrid().getHorizontalLinePaint()
					.setColor((int) Color.rgb(179, 147, 197));
			chart.getCategoryAxis()
					.getAxisPaint()
					.setColor(
							chart.getPlotGrid().getHorizontalLinePaint()
									.getColor());
			chart.getCategoryAxis()
					.getAxisPaint()
					.setStrokeWidth(
							chart.getPlotGrid().getHorizontalLinePaint()
									.getStrokeWidth());

			// ���彻����ǩ��ʾ��ʽ,�ر�ע,������ͼ�������ԣ����Է��ظ�ʽΪ: xֵ,yֵ
			// �����з�������
			chart.setDotLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					// TODO Auto-generated method stub
					String label = "[" + value + "]";
					return (label);
				}

			});
			// ����
			// chart.setTitle("Spline Chart");
//			chart.addSubtitle("�շ�������KWh��");
			chart.setTitleAlign(XEnum.ChartTitleAlign.LEFT);
			// ����������
			chart.ActiveListenItemClick();
			// Ϊ���ô�������������������5px�ĵ��������Χ
			chart.extPointClickRange(5);

			// ��ʾƽ������
			chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {
		// ��1�����ݼ�

		// ��2�����ݼ�
		linePoint1.put(40d, 50d);
		linePoint1.put(55d, 55d);

		linePoint1.put(60d, 65d);
		linePoint1.put(65d, 85d);

		linePoint1.put(72d, 70d);
		linePoint1.put(85d, 68d);

		SplineData dataSeries2 = new SplineData("", linePoint1,
				(int) Color.rgb(255, 165, 132));

		// dataSeries2.setDotStyle(XEnum.DotStyle.HIDE);
		
		
//		dataSeries2.setDotStyle(XEnum.DotStyle.RING);
//		dataSeries2.getDotLabelPaint().setColor(Color.RED);
//		dataSeries2.getDotLabelPaint().setTextAlign(Align.LEFT);
		dataSeries2.getLinePaint().setStrokeWidth(2);
		dataSeries2.setLabelVisible(false);	
		dataSeries2.getDotLabelPaint().setTextAlign(Align.LEFT);
		
		// �趨����Դ
		// chartData.add(dataSeries1);
		chartData.add(dataSeries2);
	}
	public void setChartDataSet(LinkedHashMap<Double, Double> linePoint1,LinkedHashMap<Double, Double> linePoint2,List<String> landName){
		this.linePoint1 = linePoint1;
		this.linePoint2 = linePoint2;
		SplineData dataSeries1 = new SplineData(landName.get(0), linePoint1,
				(int)Color.rgb(0,139,69) );
//		dataSeries2.setDotStyle(XEnum.DotStyle.HIDE);
//		dataSeries2.setDotStyle(XEnum.DotStyle.RING);
//		dataSeries2.getDotLabelPaint().setColor(Color.RED);

		dataSeries1.getLinePaint().setStrokeWidth(2);
		dataSeries1.setLabelVisible(false);	
		dataSeries1.getDotLabelPaint().setTextAlign(Align.LEFT);
		
		
		SplineData dataSeries2 = new SplineData(landName.get(1), linePoint2,
				(int)Color.rgb(238,118,0) );
//		dataSeries2.setDotStyle(XEnum.DotStyle.HIDE);
//		dataSeries2.setDotStyle(XEnum.DotStyle.RING);
//		dataSeries2.getDotLabelPaint().setColor(Color.RED);

		dataSeries2.getLinePaint().setStrokeWidth(2);
		dataSeries2.setLabelVisible(false);	
		dataSeries2.getDotLabelPaint().setTextAlign(Align.LEFT);
		// �趨����Դ
		chartData.add(dataSeries1);
		chartData.add(dataSeries2);
	}
	private void chartLabels() {
		labels.add("04:00");
		labels.add("05:00");
		labels.add("06:00");
		labels.add("07:00");
		labels.add("08:00");
		labels.add("09:00");
		labels.add("10:00");
		labels.add("11:00");
		labels.add("12:00");
		labels.add("13:00");
		labels.add("14:00");
		labels.add("15:00");
	}

	public void setChartLabels(LinkedList<String> labels) {
		this.labels = labels;
	}

	public void setVerData(double max , double min ,double step,int size) {
		// ����ϵ
		// ���������ֵ
		chart.getDataAxis().setAxisMax(max);
		chart.getDataAxis().setAxisMin(min);
		// ������̶ȼ��
		chart.getDataAxis().setAxisSteps(step);
		
		// ��ǩ�����ֵ
		chart.setCategoryAxisMax(size-1);
		// ��ǩ����Сֵ
		chart.setCategoryAxisMin(0);
	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public List<XChart> bindChart() {
		// TODO Auto-generated method stub
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		return lst;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		super.onTouchEvent(event);

		// if(event.getAction() == MotionEvent.ACTION_UP)
		// {
		// triggerClick(event.getX(),event.getY());
		// }
		return false;
	}

	// ��������
	private void triggerClick(float x, float y) {
		PointPosition record = chart.getPositionRecord(x, y);
		if (null == record)
			return;

		SplineData lData = chartData.get(record.getDataID());
		LinkedHashMap<Double, Double> linePoint = lData.getLineDataSet();
		int pos = record.getDataChildID();
		int i = 0;
		Iterator it = linePoint.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();

			if (pos == i) {
				Double xValue = (Double) entry.getKey();
				Double yValue = (Double) entry.getValue();

				Toast.makeText(
						this.getContext(),
						record.getPointInfo() + " Key:" + lData.getLineKey()
								+ " Current Value(key,value):"
								+ Double.toString(xValue) + ","
								+ Double.toString(yValue), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			i++;
		}// end while

	}

}
