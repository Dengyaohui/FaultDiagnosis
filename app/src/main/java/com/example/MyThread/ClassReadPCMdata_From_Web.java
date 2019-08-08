package com.example.MyThread;

import android.annotation.SuppressLint;
import android.media.AudioRecord;

import com.example.Activity.GlobleVariable;
import com.example.FileUtils.ClassShowCurData;
import com.example.Web.WebDataReceive;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 从云端获取数据,并定时保存原始数据，无量纲指标数据
 */

public class ClassReadPCMdata_From_Web extends Thread {

	private AudioRecord audioRecord;
	public static float[] tmpBuf;
	public static float[] tmpBufFromWeb = new float[50];
//	public static float[] tmpBufFromWeb = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	private int recBufSize;

	/**
	 * X轴缩小比例
	 */
	public int rateX = 1;

	/**
	 * Y轴缩小比例
	 */
	public int rateY = 2;

	private Timer timer = new Timer();

	/**
	 * 用于记录每次存放读取到的数据的缓存区的长度
	 */
	public static int old_tmpBufLength = 0;

	/**
	 * 用于保存接收的Json数据
	 * 电压值 volet ；时间 create_time ；烈度 V_rms ;脉冲 Plusevalue ;欲度 Marginvalue ;峰值 Ppeakvalue ;峭度 Kurtuosis ;
	 */
	public static float[] volet = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public static String create_time = "0000-00-00 00:00：00";
	public static String[] DataFromWeb = {"0.000","0.000","0.000","0.000","0.000"};

	public static float[] DimensionlessDataFromWeb = {0.000f,0.000f,0.000f,0.000f,0.000f,0.000f};

	public ClassReadPCMdata_From_Web(AudioRecord audioRecord, int recBufSize) {
		this.audioRecord = audioRecord;
		this.recBufSize = recBufSize;
		timer.schedule(timerTask, 1000, 1000);  //timer.schedule(task, delay, period) 	delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
	}

	public void run() {
		try {
			short[] buffer = new short[recBufSize];
			//开始录制
			audioRecord.startRecording();
			while (ClassShowCurData.isRecording) {
				int bufferReadResult = audioRecord.read(buffer, 0, recBufSize); //读取1024个,返回512,bufferReadResult = 640
				//将原来的数据缩小到rateX分之一
				tmpBuf = new float[bufferReadResult / rateX];
				old_tmpBufLength = bufferReadResult / rateX;

				for (int i = 0, ii = 0; i < tmpBuf.length; i++, ii = i * rateX) {
					tmpBuf[i] = (float) buffer[ii];
				}

				//同一时刻最多只有一个线程执行这段代码，避免重复数据
				synchronized (ClassShowCurData.inBuf) {
					ClassShowCurData.inBuf.add(tmpBuf);
				}
			}

			//停止后删除任务，否则一直保存同一个tmpBuf的值
			timerTask.cancel();
		} catch (Exception e) {
			System.out.println("Error:------->" + e.toString());
		}
	}

	/**
	 * 每秒保存一次数据，采样了五分钟再将该数据保存到另一空间
	 */
	TimerTask timerTask = new TimerTask() {

		//TODO:作用是什么，使用的场景、条件、方法？
		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {

			if (ClassShowCurData.isRecording) {

					WebDataReceive.ReceiveDataFromWeb();

//					//TODO：获取时间？可否有其他方法？
//					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//					Date curDate = new Date(System.currentTimeMillis());
//
//					String curTime = simpleDateFormat.format(curDate);
					String curTime = create_time;
					System.out.println("Save Data at Time: " + curTime);

					tmpBufFromWeb = volet;
//					for (int i = 0;i < tmpBufFromWeb.length;i ++){
//						Log.e("tmpBufFromWeb>>>>>>>>>>>", String.valueOf(tmpBufFromWeb[i]));
//					}
				getData(DimensionlessDataFromWeb,DataFromWeb);

//					for (int i = 0;i<6;i++){
//						Log.e("DimensionlessDataFromWeb>>>>>>>>>>>", String.valueOf(DimensionlessDataFromWeb[i]));
//					}

					//采集中
					//保存原始数据
					new ClassSavePCMdata(tmpBufFromWeb, "每秒原始数据", curTime + ".pcm", GlobleVariable.PER_1S_HISTORY_FILE_MAX_NUM).start();
					//计算并保存无量纲指标值
					new ClassSaveDimensionless_Parameter_Data(DimensionlessDataFromWeb, "每秒无量纲指标", curTime + ".txt", GlobleVariable.PER_1S_DIMENSION_LESS_FILE_MAX_NUM).start();

					if (GlobleVariable.WebSecondCount == 300) {
						new ClassSavePCMdata(tmpBufFromWeb, "每5分钟原始数据", curTime + ".pcm", GlobleVariable.PER_5MIN_HISTORY_FILE_MAX_NUM).start();
						new ClassSaveDimensionless_Parameter_Data(DimensionlessDataFromWeb, "每5分钟无量纲指标", curTime + ".txt", GlobleVariable.PER_5MIN_DIMENSION_LESS_FILE_MAX_NUM).start();
						GlobleVariable.SecondCount = 0;
					}
				}
				GlobleVariable.WebSecondCount++;

//			if (GlobleVariable.WebSecondCount <= volet.size()) {
//				// TODO Auto-generated method stub
//				GlobleVariable.WebSecondCount++;
////				Log.e("读秒>>>>>>>>>>>>>>>>>>>", String.valueOf(GlobleVariable.WebSecondCount));
//			}else {
//				GlobleVariable.WebSecondCount = 0;
//				WebDataReceive.ReceiveDataFromWeb();
//			}
		}
	};
	private void getData(float[] DimensionlessDataFromWeb,String[] DataFromWeb){
		for(int i =0; i < DimensionlessDataFromWeb.length - 1; i ++){
			try {
				DimensionlessDataFromWeb[i] = Float.parseFloat(DataFromWeb[i]);
			} catch (Exception e) {
				DimensionlessDataFromWeb[i] = 0.000f;
				System.out.println("接收到不规范数据------->" + e.toString());
			}
		}
		DimensionlessDataFromWeb[5] =volet[0];
	}
}
