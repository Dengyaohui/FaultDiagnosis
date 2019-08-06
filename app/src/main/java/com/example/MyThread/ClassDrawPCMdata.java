package com.example.MyThread;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.example.FileUtils.ClassShowCurData;

import java.util.ArrayList;

/**
 * 绘制图形线程
 * @author Administrator
 *
 */
public class ClassDrawPCMdata extends Thread{

	/**
	 * X轴缩小比例
	 */
	public int rateX = 4;
	/**
	 * Y轴缩小比例
	 */
	public int rateY = 4;
	/**
	 * Y轴基线
	 */
	public int baseLine = 0;
	/**
	 * 上次绘制的x轴坐标
	 */
	private int oldX = 0;
	/**
	 * 上次绘制的y轴坐标
	 */
	private float oldY = 0;
	/**
	 * 画板
	 */
	private SurfaceView sfv = null;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 当前画图所在屏幕x轴的坐标
	 */
	private int X_index = 0;
	private float Y_index = 0;

	private short[] historyBuf=null;

	public ClassDrawPCMdata(SurfaceView sfv, Paint mPaint, int rateY, int baseLine){
		this.sfv = sfv;
		this.mPaint = mPaint;
		this.rateY = rateY;
		this.baseLine = baseLine;
		oldY = baseLine;
	}
/*	public ClassDrawPCMdata(SurfaceView sfv, Paint mPaint, int rateY, int baseLine, short[] historyBuf){
		this.sfv = sfv;
		this.mPaint = mPaint;
		this.rateY = rateY;
		this.baseLine = baseLine;
		this.historyBuf = historyBuf;
		oldY = baseLine;
	}*/

	/**
	 * 绘制实时数据
	 * @param sfv       画板
	 * @param start     x轴开始的位置
	 * @param Y_index
	 * @param buffer    缓存区
	 * @param rate      y轴数据缩小的比例
	 * @param baseLine  y轴基线
	 */
	void SimpleDrawCur(SurfaceView sfv, int start, float Y_index, float[] buffer, int rate, int baseLine){
		if(start == 0)
			oldX = 0;

		//获取画布
		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(start, 0, start+buffer.length, sfv.getHeight()));
		//清除背景(用什么颜色)
		canvas.drawColor(Color.rgb(37, 40, 46));

		float y;
		for (int i = 0; i < buffer.length; i++) {
			int x = (i+start)*1;
			//调节缩小比例，基准线
			y = ((-buffer[i])/rate+baseLine);
			canvas.drawLine(oldX, oldY, x, y, mPaint);
			oldX = x;
			oldY = y;
			this.Y_index = oldY;
		}
		//解锁画布并提交画好的图像
		sfv.getHolder().unlockCanvasAndPost(canvas);
	}

	/**
	 * 绘制历史数据
	 * @param sfv
	 * @param start
	 * @param Y_index
	 * @param buffer
	 * @param rate
	 * @param baseLine
	 */
	void SimpleDrawHistory(SurfaceView sfv, int start, float Y_index, short[] buffer, int rate, int baseLine){
		if(start == 0)
			oldX = 0;

		//获取画布
		Canvas canvas = sfv.getHolder().lockCanvas(new Rect(start, 0, sfv.getWidth(), sfv.getHeight()));
		//清除背景
		canvas.drawColor(Color.BLACK);

		int y;
		for (int i = 1; i < buffer.length; i++) {
			int x = (i+start)*3;//根据屏幕大小调节波形大小
			//调节缩小比例，基准线
			y = ((-buffer[i])/rate+baseLine);
			canvas.drawLine(oldX, oldY, x, y, mPaint);
			oldX = x;
			oldY = y;
		}
		//解锁画布并提交画好的图像
		sfv.getHolder().unlockCanvasAndPost(canvas);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		//没有传进历史数据，即现在是实时采集显示
		if(historyBuf==null){
			while(ClassShowCurData.isRecording){
				ArrayList<float[]> buf;
				synchronized (ClassShowCurData.inBuf) {
					//输入数据缓存区无数据就跳过下面语句再一次循环
					if(ClassShowCurData.inBuf.size()==0)
						continue;
					//将当前缓存中的数据复制一份并返回给buf
					buf = (ArrayList<float[]>) ClassShowCurData.inBuf.clone();
					//连续画3组数据后就清空缓存区
					if(ClassShowCurData.inBuf.size() >3)
						ClassShowCurData.inBuf.clear();
				}
				for (int i = 0; i < buf.size(); i++) {
					float[] tmpBuf =  buf.get(i);
					//将当前缓存数据绘制出来
					if(tmpBuf.length>0)
						SimpleDrawCur(sfv, X_index, Y_index, tmpBuf, rateY, baseLine);
					X_index = X_index+tmpBuf.length;
//					Y_index = tmpBuf[tmpBuf.length-1];
					if(X_index>sfv.getWidth())
						X_index = 0;
				}
			}
		}
		//调出历史记录显示
		else {
			try {
				SimpleDrawHistory(sfv, X_index, Y_index, historyBuf, rateY, baseLine);
			} catch (Exception e) {
				System.out.println("Draw data error ------>"+e.toString());
			}
		}
	}


}
