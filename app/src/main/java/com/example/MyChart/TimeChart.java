package com.example.MyChart;

import java.io.File;
import java.util.Date;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.Activity.GlobleVariable;

import android.graphics.Color;
import android.graphics.Paint.Align;

public class TimeChart {

	/**
	 * 原始曲线颜色
	 */
	private int color = Color.CYAN;
	/**
	 * 原始折点形状(圆点)
	 */
	private PointStyle pointStyle = PointStyle.POINT;
	/**
	 * 图表标题
	 */
	private String title = "图表标题";
	/**
	 * 原始X轴标签
	 */
	private String X_label = "X轴标签";
	/**
	 * 原始Y轴标签
	 */
	private String Y_label = "Y轴标签";
	
	/**
	 * 默认构造函数
	 */
	public TimeChart(){
		
	}
	/**
	 * 带参数构造函数
	 * @param color      线条颜色
	 * @param pointStyle 折点形状
	 * @param title      图表标题
	 * @param X_label    横坐标标签
	 * @param Y_label    纵坐标标签
	 */
	public TimeChart(int color, PointStyle pointStyle,String title, String X_label, String Y_label){
		super();
		this.color = color;
		this.pointStyle = pointStyle;
		this.title = title;
		this.X_label = X_label;
		this.Y_label = Y_label;
	}
	
	/**
	 * 创建渲染器
	 * @return
	 */
	public XYMultipleSeriesRenderer createRenderer(){
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		
		XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
		seriesRenderer.setColor(color);
		seriesRenderer.setPointStyle(pointStyle);
		seriesRenderer.setFillPoints(true);
		seriesRenderer.setLineWidth(3);
		
		renderer.addSeriesRenderer(seriesRenderer);
		
		renderer.setChartTitle(title);
		renderer.setXTitle(X_label);
		renderer.setYTitle(Y_label);
		renderer.setYAxisMax(5);
		renderer.setYAxisMin(-5);
		renderer.setAxesColor(Color.rgb(128, 138, 135));        //坐标轴颜色
		renderer.setLabelsColor(Color.WHITE);                   //标签颜色
		renderer.setMarginsColor(Color.rgb(37, 40, 46));        //图表周围颜色
		renderer.setGridColor(Color.DKGRAY);                    //表格颜色
		renderer.setBackgroundColor(Color.WHITE);
		
		renderer.setXLabels(15);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.RIGHT);
//		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setChartTitleTextSize(20);
		renderer.setAxisTitleTextSize(16);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		
		renderer.setShowLegend(true);                           //是否显示图例
		renderer.setShowGrid(true);                             //是否显示网格
		renderer.setPointSize((float)2);
		renderer.setLegendTextSize(20);                         //图例文字大小
		
		renderer.setClickEnabled(false);
//		renderer.setZoomEnabled(true);                          //是否显示放大缩小按钮
		renderer.setPanEnabled(true, false);                     //是否左右上下可移动
		
		renderer.setMargins(new int[]{20,30,15,20});
		
		return renderer;
	}

	/**
	 * 创建数据集(无量纲指标用)
	 * @param data     数据
	 * @param dates    横坐标时刻
	 * @param dataType 数据类型
	 * @return
	 */
	public XYMultipleSeriesDataset createDataset(float[] data, Date[] dates, int dataType, int showType){
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		
		String seriesTitle = "";
		switch (dataType) {
		case 0:
			seriesTitle = "烈度";
			break;
		case 1:
			seriesTitle = "脉冲";
			break;
		case 2:
			seriesTitle = "欲度";
			break;
		case 3:
			seriesTitle = "峰值";
			break;
		case 4:
			seriesTitle = "峭度";
			break;
		case 5:
			seriesTitle = "波形";
			break;
		default:
			break;
		}
		
		
		//旧点在前，新点在后
//		for (int i = 0,j = data.length-1 ; i < dates.length&&j>=0 ; i++,j--) {
//			timeSeries.add(dates[i], data[j]);
//		}
		
		//只需要x坐标点数来生成数据集
		if (showType==GlobleVariable.SHOW_CUR_1MIN) {
			XYSeries xySeries = new XYSeries(seriesTitle);
			int xMax = 60;
			//把指定前多少个最新的数据放到缓存中使用
			float[] tempBuf = new float[xMax];
			for (int i = 0; i < xMax&& i<data.length; i++) {
				tempBuf[i] = data[i];
			}
			//把最旧的点放在x轴前面
			for (int i = 0,j=tempBuf.length-1; i < xMax; i++,j--) {
				xySeries.add(i, tempBuf[j]);
			}
			dataset.addSeries(xySeries);
		}
		else if(showType == GlobleVariable.SHOW_CUR_5MIN){
			XYSeries xySeries = new XYSeries(seriesTitle);
			int xMax = 300;
			//把指定前多少个最新的数据放到缓存中使用
			float[] tempBuf = new float[xMax];
			for (int i = 0; i < xMax&& i<data.length; i++) {
				tempBuf[i] = data[i];
			}
			//把最旧的点放在x轴前面
			for (int i = 0,j=tempBuf.length-1; i < xMax; i++,j--) {
				xySeries.add(i, tempBuf[j]);
			}
			dataset.addSeries(xySeries);
		}
		//需要Date[]来生成数据集
		else {
			TimeSeries timeSeries = new TimeSeries(seriesTitle);
			for (int i = 0; i < dates.length; i++) {
				timeSeries.add(dates[i], data[i]);
			}
			dataset.addSeries(timeSeries);
		}
		
		return dataset;
	}

	/**
	 * 创建数据集（原始数据用）
	 * @param data        原始数据是short类型的
	 * @param seriesTitle 图表标签
	 * @return
	 */
	public XYMultipleSeriesDataset createDataset(short[] data,String seriesTitle){
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries xySeries = new XYSeries(seriesTitle);
		//把最旧的点放在x轴前面
		for (int i = 0; i < data.length; i++) {
			xySeries.add(i, data[i]);
		}
		dataset.addSeries(xySeries);
		return dataset;
	}
}
