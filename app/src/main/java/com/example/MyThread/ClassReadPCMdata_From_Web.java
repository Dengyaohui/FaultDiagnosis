package com.example.MyThread;

import android.annotation.SuppressLint;
import android.media.AudioRecord;

import com.example.Activity.GlobleVariable;
import com.example.FileUtils.ClassShowCurData;
import com.example.Web.WebDataReceive;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 从云端获取数据,并定时保存原始数据，无量纲指标数据
 */

public class ClassReadPCMdata_From_Web extends Thread {

	private AudioRecord audioRecord;
	public static short[] tmpBuf;
	public static short[] tmpBufFromWeb;
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
	 * 电压值 volet ；时间 create_time ；
	 */
	public static ArrayList<Short> volet = new ArrayList<>();
	public static ArrayList<String> create_time = new ArrayList<>();

	public ClassReadPCMdata_From_Web(AudioRecord audioRecord, int recBufSize) {
		this.audioRecord = audioRecord;
		this.recBufSize = recBufSize;
		timer.schedule(timerTask, 1000, 1000);  //1秒后每1秒保存一次数据
	}

	public void run() {
		try {
			short[] buffer = new short[recBufSize];
			//开始录制
			audioRecord.startRecording();
			while (ClassShowCurData.isRecording) {
				int bufferReadResult = audioRecord.read(buffer, 0, recBufSize); //读取1024个,返回512,bufferReadResult = 640
				//将原来的数据缩小到rateX分之一
				tmpBuf = new short[bufferReadResult / rateX];
				old_tmpBufLength = bufferReadResult / rateX;

				for (int i = 0, ii = 0; i < tmpBuf.length; i++, ii = i * rateX) {
					tmpBuf[i] = buffer[ii];
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

				if (GlobleVariable.SecondCount < volet.size()) {
					String curTime = create_time.get(GlobleVariable.SecondCount);
					System.out.println("Save Data at Time: " + curTime);

					tmpBufFromWeb = new short[volet.size()];
					tmpBufFromWeb[GlobleVariable.SecondCount] = volet.get(GlobleVariable.SecondCount);

					//采集中
					//保存原始数据
					new ClassSavePCMdata(tmpBufFromWeb, "云端原始数据", curTime + ".pcm", GlobleVariable.PER_1S_HISTORY_FILE_MAX_NUM).start();
					//计算并保存无量纲指标值
					new ClassSaveDimensionless_Parameter_Data(tmpBufFromWeb, "云端无量纲指标", curTime + ".txt", GlobleVariable.PER_1S_DIMENSION_LESS_FILE_MAX_NUM).start();
					//每5分钟保存到另一文件夹下
					if (GlobleVariable.SecondCount == volet.size()) {
						new ClassSavePCMdata(tmpBufFromWeb, "云端每5分钟原始数据", curTime + ".pcm", GlobleVariable.PER_5MIN_HISTORY_FILE_MAX_NUM).start();
						new ClassSaveDimensionless_Parameter_Data(tmpBufFromWeb, "云端每5分钟无量纲指标", curTime + ".txt", GlobleVariable.PER_5MIN_DIMENSION_LESS_FILE_MAX_NUM).start();
						GlobleVariable.SecondCount = 0;
						WebDataReceive.ReceiveDataFromWeb();
					}
				}
			}
			if (GlobleVariable.SecondCount < volet.size()) {
				// TODO Auto-generated method stub
				GlobleVariable.SecondCount++;
			}
		}
	};
}
