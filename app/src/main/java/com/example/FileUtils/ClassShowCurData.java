package com.example.FileUtils;

import android.graphics.Paint;
import android.media.AudioRecord;
import android.view.SurfaceView;

import com.example.Activity.GlobleVariable;
import com.example.MyThread.ClassDrawPCMdata;
import com.example.MyThread.ClassReadPCMdata_From_Mic;
import com.example.MyThread.ClassReadPCMdata_From_Web;
import com.example.MyThread.ClassSaveDimensionless_Parameter_Data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 原始数据显示类，包含从麦克风获取数据的线程和SurfaceView绘图线程的实现，两个线程同步操作
 * @author Administrator
 *
 */
public class ClassShowCurData {

	/**
	 * 将每帧数据添加到该列表中
	 */
	public static ArrayList<short[]> inBuf = new ArrayList<short[]>();
	/**
	 * 线程控制标记
	 */
	public static boolean isRecording = false;
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

	Timer timer = new Timer();
	private SaveNullDataTimerTask timerTask = null;
	/**
	 * 初始化
	 */
	public void initOscilloscope(int rateX, int rateY){
		this.rateX = rateX;
		this.rateY = rateY;
	}
	/**
	 * 开始
	 * @param audioRecord 传进设置好的audioRecord
	 * @param recBufSize  AudioRecord的最小缓存大小MinBufferSize
	 * @param sfv
	 * @param mPaint
	 */
	public void Start(AudioRecord audioRecord, int recBufSize, SurfaceView sfv, Paint mPaint){
		isRecording = true;

		if(timerTask!=null)
			timerTask.cancel();

		new ClassReadPCMdata_From_Mic(audioRecord, recBufSize).start();//读数据并且每隔1秒保存一次（原始数据）
		//左上角绿色麦克风波形图绘制
		new ClassDrawPCMdata(sfv, mPaint, rateY, baseLine).start();    //绘制实时数据图(surfaceview上显示)
		//绘制实时数据波形图(chartview上显示)
	}
	/**
	 * 开始从后台拉取数据
	 */
	public void StartForWeb(int recBufSize, SurfaceView sfv, Paint mPaint){
		isRecording = true;

		if(timerTask!=null)
			timerTask.cancel();

		new ClassReadPCMdata_From_Web(recBufSize).start();//读数据并且每隔1秒保存一次（原始数据）
		new ClassDrawPCMdata(sfv, mPaint, rateY, baseLine).start();    //绘制实时数据图(surfaceview上显示)
		//绘制实时数据波形图(chartview上显示)
	}
	/**
	 * 暂停
	 */
	public void Stop(){
		isRecording = false;
		inBuf.clear();                       //清空缓存
		timerTask = new SaveNullDataTimerTask();
		timer.schedule(timerTask, 0, 1000);  //停止后每秒也要保存数据，只是这些数据全是0
	}

	/**
	 * 该定时任务用于采集停止的时候保存一些无意义的0数据(只是保存无量纲指标，原始数据不需要保存无意义的数据)
	 * @author Administrator
	 *
	 */
	class SaveNullDataTimerTask extends TimerTask{

		@Override
		public void run() {
			//共用一个计数点，因为是一起算5分钟的
			GlobleVariable.SecondCount++;

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date curDate = new Date(System.currentTimeMillis());
			String curTime = simpleDateFormat.format(curDate);

			//暂停中
			System.out.println("机器暂停采集~~~");
			//根据原来原始数据缓存区的大小新建一个数组并全用0填充
			short[] newtmpBuf = new short[ClassReadPCMdata_From_Mic.old_tmpBufLength];
			for (int i = 0; i < newtmpBuf.length; i++) {
				newtmpBuf[i] = 0;
			}

//			new ClassSavePCMdata(newtmpBuf, "每秒原始数据", curTime+".pcm", GlobleVariable.PER_1S_HISTORY_FILE_MAX_NUM).start();
			//计算并保存无量纲指标值
			new ClassSaveDimensionless_Parameter_Data(newtmpBuf, "每秒无量纲指标", curTime+".txt", GlobleVariable.PER_1S_DIMENSION_LESS_FILE_MAX_NUM).start();

			//每5分钟保存到另一文件夹下
			if(GlobleVariable.SecondCount==300){
//				new ClassSavePCMdata(newtmpBuf, "每5分钟原始数据", curTime+".pcm", GlobleVariable.PER_5MIN_HISTORY_FILE_MAX_NUM).start();
				new ClassSaveDimensionless_Parameter_Data(newtmpBuf, "每5分钟无量纲指标", curTime+".txt", GlobleVariable.PER_5MIN_DIMENSION_LESS_FILE_MAX_NUM).start();
				GlobleVariable.SecondCount = 0;
			}
		}

	}

}
