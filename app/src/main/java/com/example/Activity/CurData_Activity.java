package com.example.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.Adapter.MyReportListViewAdapter;
import com.example.FileUtils.ClassShowCurData;
import com.example.FileUtils.Class_SortFileAtModifiedTime;
import com.example.MyChart.TimeChart;
import com.example.MyPacketClass.DimensionlessReport;
import com.example.MyUtilClass.Report_first_item_pressed;
import com.example.MyUtilClass.Report_second_item_pressed;
import com.example.MyUtilClass.Report_third_item_pressed;
import com.example.MyUtilClass.Show_first_item_pressed;
import com.example.MyUtilClass.Show_first_item_pressed.RestartTimerTask_CallBack;
import com.example.MyUtilClass.Show_second_item_pressed;
import com.example.MyUtilClass.Show_third_item_pressed;
import com.example.MyUtilClass.Util;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuWidget;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 实时采集数据界面
 * @author Administrator
 *
 */
public class CurData_Activity extends Activity implements OnClickListener{

	private Context context = this;
	/**
	 * 采集数据显示的画布
	 */
	private SurfaceView sfv;
	/**
	 * 画笔
	 */
	Paint mPaint;
	/**
	 * 音频口操作
	 */
	AudioRecord audioRecord;
	/**
	 * 原始数据的显示类
	 */
	ClassShowCurData classShowCurData = new ClassShowCurData();
	/**
	 * 分辨率
	 */
	static final int frequency = 8000;
	/**
	 * 频道配置
	 */
	static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	/**
	 * 音频信号编码格式(16位pcm编码)
	 */
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	/**
	 * x轴缩小比例最大值，x轴数据量巨大，容易产生刷新延时
	 */
	static final int xMax = 1;
	/**
	 * x轴缩小比例最小值
	 */
	static final int xMin = 1;
	/**
	 * y轴缩小比例最大值
	 */
	static final int yMax = 20;
	/**
	 * y轴缩小比例最小值
	 */
	static final int yMin = 10;
	/**
	 * 录音最小buffer大小
	 */
	int recBufSize;
	/**
	 * 定时器(用于更新波形数据)
	 */
	private Timer timer = new Timer();
	private UpdateCurDataInChartView_TimerTask updateCurDataInChartView_TimerTask;
	private UpdateCurDataInChartViewFromWeb_TimerTask updateCurDataInChartViewFromWeb_timerTask;
	/**
	 * 定时器(用于更新诊断报告)
	 */
	private Timer timer_for_report = new Timer();
	private Update_reportData update_reportData;
	private Update_reportWebData update_reportWebData;
	/**
	 * 报告列表
	 */
	private List<DimensionlessReport> report_list = new ArrayList<>();
	private List<DimensionlessReport> webreport_list = new ArrayList<>();
	private ListView reportListView=null;
	MyReportListViewAdapter listViewAdapter = null;
	/**
	 * 用于保存当前数据
	 */
	private short[] curData;
	/**
	 * 用于保存当前无量纲指标数据
	 */
	private float[] curDimensionlissData;


	/**
	 * 普通按钮
	 */
	private Button sceneButton,stopButton,webButton;
	private LinearLayout mChartLayout = null;
	/**
	 * 时间等信息显示标签
	 */
	private TextView labelTextView = null;
	private String curlabel = null;
	/**
	 * 无量纲指标标签
	 */
	private TextView dataValueTextView1 = null;
	private TextView dataValueTextView2 = null;
	private TextView dataValueTextView3 = null;
	private TextView dataValueTextView4 = null;
	private TextView dataValueTextView5 = null;
	private TextView dataValueTextView6 = null;
	/**
	 * 是否正在采集提示标签
	 */
	private TextView collctionTips = null;
	/**
	 * 图表将创建在该view上
	 */
	public GraphicalView mGraphicalView=null;
	/**
	 * 时间图表渲染器
	 */
	private TimeChart timeChart = null;

	/**
	 * 底部功能按钮
	 */
	private ImageButton report_Btn=null;
	private ImageButton collction_set_Btn=null;
	private ImageButton pause_Btn=null;private TextView pauseLabel = null;
	/**
	 * 底部按钮标志线
	 */
	private ImageView pause_underLine = null,set_underLine = null,report_underLine = null;
	/**
	 * 是否暂停更新按钮
	 */
	private boolean ifPause_update = false;

	/**
	 * 圆环操作栏
	 */
	private RadialMenuWidget pieMenu;
	public RadialMenuItem menuShowItem, menuCloseItem, menuCollectionItem,menuReportItem;
	public RadialMenuItem firstChildItem_Collection, secondChildItem_Collection, thirdChildItem_Collection;
	public RadialMenuItem firstChildItem_ShowSet, secondChildItem_ShowSet, thirdChildItem_ShowSet;
	public RadialMenuItem firstChildItem_Report, secondChildItem_Report, thirdChildItem_Report;
	private List<RadialMenuItem> children_collection = new ArrayList<RadialMenuItem>();
	private List<RadialMenuItem> children_show = new ArrayList<RadialMenuItem>();
	private List<RadialMenuItem> children_report = new ArrayList<RadialMenuItem>();

	/**
	 * 滑动显示窗口类
	 */
	private MenuDrawer mReportDrawer;

	/**
	 * 按钮锁
	 * */
	private boolean isrReport = false;

	/**
	 * 工具类
	 * */
	private Util util;

	public CurData_Activity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.cur_data_activity);
		/*给当前Activity贴上滑动窗口,方向为...*/
		mReportDrawer=MenuDrawer.attach(this,Position.LEFT);
		mReportDrawer.setContentView(R.layout.cur_data_activity);         //其实函数内是为该activity设置布局的
		mReportDrawer.setMenuView(R.layout.report_menu);

		//初始化设置参数
		init_Setting();
		//初始化各对象
		initUI();
		//对音频口采集做准备
		initAudioRecord();
		//初始化圆环操作栏
		initRadialWidget();
	}

	/**
	 * 该Activity退出要删除运行着的线程
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 初始化设置参数，获取上一次的设置参数
	 */
	private void init_Setting(){
		SharedPreferences sharedPreferences = getSharedPreferences("settings", 0);
		GlobleVariable.UPDATE_CUR_DATA_PER_TIME = sharedPreferences.getInt("UPDATE_CUR_DATA_PER_TIME", 1000);
		GlobleVariable.UPDATE_REPORT_PER_TIME = sharedPreferences.getInt("UPDATE_REPORT_PER_TIME", 1000);
		GlobleVariable.CREATE_REPORT_PER_COUNT = sharedPreferences.getInt("CREATE_REPORT_PER_COUNT", 1);
		GlobleVariable.LIEDU_HIGH_WARING = sharedPreferences.getFloat("LIEDU_HIGH_WARING", (float) 5.000);
		GlobleVariable.LIEDU_LOW_WARING = sharedPreferences.getFloat("LIEDU_LOW_WARING", (float) -5.000);
		GlobleVariable.MAICHONG_HIGH_WARING = sharedPreferences.getFloat("MAICHONG_HIGH_WARING", (float) 5.000);
		GlobleVariable.MAICHONG_LOW_WARING = sharedPreferences.getFloat("MAICHONG_LOW_WARING", (float) -5.000);
		GlobleVariable.YUDU_HIGH_WARING = sharedPreferences.getFloat("YUDU_HIGH_WARING", (float) 5.000);
		GlobleVariable.YUDU_LOW_WARING = sharedPreferences.getFloat("YUDU_LOW_WARING", (float) -5.000);
		GlobleVariable.FENGZHI_HIGH_WARING = sharedPreferences.getFloat("FENGZHI_HIGH_WARING", (float) 5.000);
		GlobleVariable.FENGZHI_LOW_WARING = sharedPreferences.getFloat("FENGZHI_LOW_WARING", (float) -5.000);
		GlobleVariable.QIAODU_HIGH_WARING = sharedPreferences.getFloat("QIAODU_HIGH_WARING", (float) 5.000);
		GlobleVariable.QIAODU_LOW_WARING = sharedPreferences.getFloat("QIAODU_LOW_WARING", (float) -5.000);
		GlobleVariable.BOXING_HIGH_WARING = sharedPreferences.getFloat("BOXING_HIGH_WARING", (float) 5.000);
		GlobleVariable.BOXING_LOW_WARING = sharedPreferences.getFloat("BOXING_LOW_WARING", (float) -5.000);

	}

	/**
	 * 定时在chartview中显示实时数据波形以及无量纲指标数值
	 */
	//现场
	public class UpdateCurDataInChartView_TimerTask extends TimerTask{

		public UpdateCurDataInChartView_TimerTask(){
		}

		@Override
		public void run() {
			if (!ifPause_update) {
				new Thread(new readCurData_From_File()).start();//当前不是处于暂停更新状态
			}
		}

	}
	//云端
	public class UpdateCurDataInChartViewFromWeb_TimerTask extends TimerTask{
		public UpdateCurDataInChartViewFromWeb_TimerTask(){

		}

		@Override
		public void run() {
			if(!ifPause_update){
				new Thread(new readCurData_From_WebFile()).start();
			}
		}
	}

	/**
	 * 定时更新诊断报告中的数据
	 */
	//现场
	class Update_reportData extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!ifPause_update) {
				updateReportHandler.sendEmptyMessage(0);
			}
		}
	}
	Handler updateReportHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updateReportList(report_list);
		}

	};
	//云端
	class Update_reportWebData extends TimerTask{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!ifPause_update) {
				updateWebReportHandler.sendEmptyMessage(0);
			}
		}
	}
	Handler updateWebReportHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updateReportList(webreport_list);
		}

	};
	/****************************************************获取最新一秒的采集数据*************************************************************************/
	//现场
	class readCurData_From_File implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			curData = getNewestSecondData(GlobleVariable.CUR_ORIGINAL_DATA_PER_1S);
			curDimensionlissData = getNewestSecondDimensionlessData(GlobleVariable.CUR_DIMENSION_LESS_PER_1S);
			if (curData!=null && curDimensionlissData!=null) {
				updateChartViewHandler.sendEmptyMessage(0);
			}

		}

	}
	//云端
	class readCurData_From_WebFile implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			curData = getNewestSecondData(GlobleVariable.WEB_CUR_ORIGINAL_DATA_PER_1S);
			curDimensionlissData = getNewestSecondDimensionlessData(GlobleVariable.WEB_CUR_DIMENSION_LESS_PER_1S);
			if (curData!=null && curDimensionlissData!=null) {
				updateChartViewHandler.sendEmptyMessage(0);
			}
		}
	}
	Handler updateChartViewHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			updateChartView();
		}

	};
	/**
	 * 读取最新一秒数据的文件并获取原始数据保存到curData数组中
	 * param fileName
	 */
	private short[] getNewestSecondData(String Filename){
		File historyFile = null;
		try {
			historyFile = new File(getNewestFile(GlobleVariable.SD_CARD_PATH+"/"+GlobleVariable.DATA_SAVE_DIR+"/"+Filename));
		} catch (Exception e) {
			System.out.println("每秒原始数据文件夹中还没有数据文件");
			return null;
		}
		//写进文件的数据是16位的，因为有正负，所以创建历史记录缓存区时长度为一半
		int length = (int)(historyFile.length()/2);
		short[] curData = new short[length];

		DataInputStream dataInputStream = null;
		try {
			InputStream inputStream = new FileInputStream(historyFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			dataInputStream = new DataInputStream(bufferedInputStream);

			int i = 0;
			while(dataInputStream.available()>0){
				curData[i] = dataInputStream.readShort();
				i++;
			}
		} catch (Exception e) {
			System.out.println("获取最新一秒原始数据失败----->"+e.toString());
		}finally{
			try {
				dataInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//显示当前数据对应的时间点
		String stringName = historyFile.getName();
		stringName = stringName.substring(0,19);
		char[] curDataTime = stringName.toCharArray();
		curDataTime[10] = ' ';
		curDataTime[13] = ':';
		curDataTime[16] = ':';

		curlabel = String.valueOf(curDataTime);

		return curData;
	}
	/**
	 * 获取保存最新那一秒数据的文件名
	 * param curDirPath
	 */
	private String getNewestFile(String originalData_DirPath){
		File curDir = new File(originalData_DirPath);
		File[] files = curDir.listFiles();
		//按时间顺序排序(新到旧)
		Class_SortFileAtModifiedTime sortFiles = new Class_SortFileAtModifiedTime(files);
		files = null;
		files = sortFiles.getSortFiles();

		return files[0].getAbsolutePath();
	}
	/**
	 * 读取最新一秒数据的文件并获取无量纲指标数据保存到curDimensionlissData数组中
	 * param fileName
	 */
	private float[] getNewestSecondDimensionlessData(String Filename){
		File historyFile = null;
		try {
			historyFile = new File(getNewestFile(GlobleVariable.SD_CARD_PATH+"/"+GlobleVariable.DATA_SAVE_DIR+"/"+Filename));
			System.out.println("当前对应无量纲指标文件："+historyFile.getName());
		} catch (Exception e) {
			System.out.println("每秒原始数据文件夹中还没有数据文件");
			return null;
		}
		//只有6种无量纲指标值
		float[] curDimensionlessData = new float[6];

		DataInputStream dataInputStream = null;
		try {
			InputStream inputStream = new FileInputStream(historyFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			dataInputStream = new DataInputStream(bufferedInputStream);

			int i = 0;
			while(dataInputStream.available()>0){
				curDimensionlessData[i] = dataInputStream.readFloat();
				i++;
			}
			//加入诊断报告(需要根据间隔时间判断是否生成报告)
			Add2ReportList(historyFile.getName(), curDimensionlessData);

		} catch (Exception e) {
			System.out.println("获取最新一秒无量纲指标数据失败----->"+e.toString());
		}finally{

			try {
				dataInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return curDimensionlessData;

	}
	/**
	 * 加入报告列表（多久加入一次可设置）
	 */
	private void Add2ReportList(String fileName, float[] dimensionless){

		GlobleVariable.CREATE_REPORT_COUNT++;//一秒计一个点
		if (GlobleVariable.CREATE_REPORT_COUNT == GlobleVariable.CREATE_REPORT_PER_COUNT) {
			//控制报告数
			if (report_list.size()>GlobleVariable.REPORTS_MAX) {
				report_list.remove(0);
			}
			report_list.add(Create_Report_use_DimensionlessData(fileName, dimensionless));

			GlobleVariable.CREATE_REPORT_COUNT = 0;
		}

	}
	/**
	 * 生成诊断报告
	 * param fileName
	 * param dimensionless
	 */
	private DimensionlessReport Create_Report_use_DimensionlessData(String fileName, float[] dimensionless){

		DimensionlessReport report = new DimensionlessReport();

		fileName = fileName.substring(0,19);
		char[] curDataTime = fileName.toCharArray();
		curDataTime[10] = ' ';
		curDataTime[13] = ':';
		curDataTime[16] = ':';
		String timeString = String.valueOf(curDataTime);
		report.setDateTime(timeString);

		report.setLiedu_data(dimensionless[0]);
		report.setMaichong_data(dimensionless[1]);
		report.setYudu_data(dimensionless[2]);
		report.setFengzhi_data(dimensionless[3]);
		report.setQiaodu_data(dimensionless[4]);
		report.setBoxing_data(dimensionless[5]);

		if (report.getLiedu_data() > GlobleVariable.LIEDU_HIGH_WARING) {
			report.setLiedu_state("过高");
		}else if(report.getLiedu_data() < GlobleVariable.LIEDU_LOW_WARING){
			report.setLiedu_state("过低");
		}else {
			report.setLiedu_state("正常");
		}

		if (report.getMaichong_data() > GlobleVariable.MAICHONG_HIGH_WARING) {
			report.setMaichong_state("过高");
		}else if(report.getMaichong_data() < GlobleVariable.MAICHONG_LOW_WARING){
			report.setMaichong_state("过低");
		}else {
			report.setMaichong_state("正常");
		}

		if (report.getYudu_data() > GlobleVariable.YUDU_HIGH_WARING) {
			report.setYudu_state("过高");
		}else if(report.getYudu_data() < GlobleVariable.YUDU_LOW_WARING){
			report.setYudu_state("过低");
		}else {
			report.setYudu_state("正常");
		}

		if (report.getFengzhi_data() > GlobleVariable.FENGZHI_HIGH_WARING) {
			report.setFengzhi_state("过高");
		}else if(report.getFengzhi_data() < GlobleVariable.FENGZHI_LOW_WARING){
			report.setFengzhi_state("过低");
		}else {
			report.setFengzhi_state("正常");
		}

		if (report.getQiaodu_data() > GlobleVariable.QIAODU_HIGH_WARING) {
			report.setQiaodu_state("过高");
		}else if(report.getQiaodu_data() < GlobleVariable.QIAODU_LOW_WARING){
			report.setQiaodu_state("过低");
		}else {
			report.setQiaodu_state("正常");
		}

		if (report.getBoxing_data() > GlobleVariable.BOXING_HIGH_WARING) {
			report.setBoxing_state("过高");
		}else if(report.getBoxing_data() < GlobleVariable.BOXING_LOW_WARING){
			report.setBoxing_state("过低");
		}else {
			report.setBoxing_state("正常");
		}

		return report;
	}

	/****************************************************更新图表波形***********************************************************************/
	private void updateChartView(){
		XYMultipleSeriesDataset dataset = timeChart.createDataset(curData, "实时采集数据波形");
		XYMultipleSeriesRenderer renderer = timeChart.createRenderer();

		fit(curData, renderer);
		mChartLayout.removeAllViews();
		mGraphicalView = ChartFactory.getLineChartView(context, dataset, renderer);
		mChartLayout.addView(mGraphicalView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		labelTextView.setText("当前数据：  "+curlabel);
		dataValueTextView1.setText(String.valueOf(curDimensionlissData[0]));
		dataValueTextView2.setText(String.valueOf(curDimensionlissData[1]));
		dataValueTextView3.setText(String.valueOf(curDimensionlissData[2]));
		dataValueTextView4.setText(String.valueOf(curDimensionlissData[3]));
		dataValueTextView5.setText(String.valueOf(curDimensionlissData[4]));
		dataValueTextView6.setText(String.valueOf(curDimensionlissData[5]));

		//不再次更新一下的话，点击列表会闪退
		if(listViewAdapter!=null){
			listViewAdapter.notifyDataSetChanged();
		}
	}
	/****************************************************更新诊断报告***********************************************************************/
	private void updateReportList(List<DimensionlessReport> list){
		if (listViewAdapter==null && list!=null) {
			listViewAdapter = new MyReportListViewAdapter(context, report_list);
			listViewAdapter.notifyDataSetChanged();
			reportListView.setAdapter(listViewAdapter);
		}else {
			listViewAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 自适应坐标轴
	 */
	private void fit(short[] data, XYMultipleSeriesRenderer mSeriiesXYRenderer){
		int max=0;
		for (int i = 0; i < data.length; i++) {
			if(Math.abs(data[i])>max)
				max = data[i];
		}
		//y轴
		mSeriiesXYRenderer.setYAxisMin(-(Math.ceil(max/10)*20));
		mSeriiesXYRenderer.setYAxisMax(Math.ceil(max/10)*20);
		mSeriiesXYRenderer.setYLabels(30);
		//x轴
		mSeriiesXYRenderer.setXAxisMax(data.length);
		mSeriiesXYRenderer.setXLabels(data.length/10);
		//根据当前情况对渲染器做一点修改
		mSeriiesXYRenderer.setPanLimits(new double[]{0,data.length+data.length/10,-(max*2),max*2});
	}

	/****************************************************系统(界面)初始化*************************************************************************/
	/**
	 * 创建界面
	 */
	@SuppressWarnings("deprecation")
	private void initUI(){

		//得到普通按钮
		sceneButton = findViewById(R.id.startButton);
		sceneButton.setOnClickListener(this);
		stopButton = findViewById(R.id.stopButton);
		stopButton.setOnClickListener(this);
		webButton = findViewById(R.id.webButton);
		webButton.setOnClickListener(this);

		//得到画布和画笔
		sfv = findViewById(R.id.surfaceView);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);
		mPaint.setStrokeWidth(1);    //画笔粗细

		//无量纲指标值标签
		dataValueTextView1 = findViewById(R.id.bottomData1);
		dataValueTextView2 = findViewById(R.id.bottomData2);
		dataValueTextView3 = findViewById(R.id.bottomData3);
		dataValueTextView4 = findViewById(R.id.bottomData4);
		dataValueTextView5 = findViewById(R.id.bottomData5);
		dataValueTextView6 = findViewById(R.id.bottomData6);

		//采集提示标签
		collctionTips = findViewById(R.id.collction_tips);

		//chartView
		labelTextView = findViewById(R.id.curdata_label_TextView);
		mChartLayout = findViewById(R.id.curDataChartView);
		timeChart = new TimeChart(Color.CYAN, PointStyle.CIRCLE, "实时采样值", "采样点", "采样值");

		//底部功能按钮
		report_Btn = findViewById(R.id.report_imageBtn);
		report_Btn.setOnClickListener(this);
		collction_set_Btn = findViewById(R.id.colletion_set_imageBtn);
		collction_set_Btn.setOnClickListener(this);
		pauseLabel = findViewById(R.id.pause_and_startLabel);
		pause_Btn = findViewById(R.id.pause_and_start_imageBtn);
		pause_Btn.setOnClickListener(this);
		//底部功能按钮标志线
		pause_underLine = findViewById(R.id.pauseUnderLine);
		set_underLine = findViewById(R.id.setUnderLine);
		report_underLine = findViewById(R.id.reportUnderLine);

		//诊断报告列表
		reportListView = findViewById(R.id.reportList);

	}
	/**
	 * 初始化音频
	 */
	private void initAudioRecord(){
		recBufSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);   // TODO  read first
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, recBufSize);
		classShowCurData.initOscilloscope(xMax, yMax);
	}

	/**
	 * 初始化圆环操作栏
	 */
	@SuppressWarnings("serial")
	private void initRadialWidget(){
		pieMenu = new RadialMenuWidget(this);

		firstChildItem_ShowSet = new RadialMenuItem(getString(R.string.Set_show_item1),getString(R.string.Set_show_item1));//刷新显示间隔
		firstChildItem_ShowSet.setOnMenuItemPressed(new Show_first_item_pressed(context,restartTimerTask_CallBack));

		secondChildItem_ShowSet = new RadialMenuItem(getString(R.string.Set_show_item2),getString(R.string.Set_show_item2));//显示范围
//		secondChildItem_ShowSet.setDisplayIcon(R.drawable.ic_launcher);
		secondChildItem_ShowSet.setOnMenuItemPressed(new Show_second_item_pressed(context));

		thirdChildItem_ShowSet = new RadialMenuItem(getString(R.string.Set_show_item3), getString(R.string.Set_show_item3));//是否显示指标线
		thirdChildItem_ShowSet.setOnMenuItemPressed(new Show_third_item_pressed(context));

		firstChildItem_Collection = new RadialMenuItem(getString(R.string.Set_collection_item1),getString(R.string.Set_collection_item1));
		firstChildItem_Collection.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
			}
		});

		secondChildItem_Collection = new RadialMenuItem(getString(R.string.Set_collection_item2),getString(R.string.Set_collection_item2));
		secondChildItem_Collection.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
			}
		});

		thirdChildItem_Collection = new RadialMenuItem(getString(R.string.Set_collection_item3), getString(R.string.Set_collection_item3));
		thirdChildItem_Collection.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
			}
		});

		firstChildItem_Report = new RadialMenuItem(getString(R.string.Set_report_item1),getString(R.string.Set_report_item1));//诊断间隔
		firstChildItem_Report.setOnMenuItemPressed(new Report_first_item_pressed(context));

		secondChildItem_Report = new RadialMenuItem(getString(R.string.Set_report_item2),getString(R.string.Set_report_item2));//更新时间
		secondChildItem_Report.setOnMenuItemPressed(new Report_second_item_pressed(context));

		thirdChildItem_Report = new RadialMenuItem(getString(R.string.Set_report_item3),getString(R.string.Set_report_item3));//警告线
		thirdChildItem_Report.setOnMenuItemPressed(new Report_third_item_pressed(context));

		//关闭操作栏item
		menuCloseItem = new RadialMenuItem(getString(R.string.close), null);
		menuCloseItem.setDisplayIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menuCloseItem.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
			@Override
			public void execute() {
				// menuLayout.removeAllViews();
				pieMenu.dismiss();
			}
		});
		//显示设置item
		menuShowItem = new RadialMenuItem(getString(R.string.Set_show), getString(R.string.Set_show));
		children_show.add(firstChildItem_ShowSet);
		children_show.add(secondChildItem_ShowSet);
		children_show.add(thirdChildItem_ShowSet);
		menuShowItem.setMenuChildren(children_show);
		//采样设置item
		menuCollectionItem = new RadialMenuItem(getString(R.string.Set_collection), getString(R.string.Set_collection));
		children_collection.add(firstChildItem_Collection);
		children_collection.add(secondChildItem_Collection);
		children_collection.add(thirdChildItem_Collection);
		menuCollectionItem.setMenuChildren(children_collection);
		//诊断设置
		menuReportItem = new RadialMenuItem(getString(R.string.Set_report), getString(R.string.Set_report));
		children_report.add(firstChildItem_Report);
		children_report.add(secondChildItem_Report);
		children_report.add(thirdChildItem_Report);
		menuReportItem.setMenuChildren(children_report);

		// pieMenu.setDismissOnOutsideClick(true, menuLayout);
		pieMenu.setAnimationSpeed(0L);
		pieMenu.setSourceLocation(0, 0);
		pieMenu.setIconSize(15, 30);
		pieMenu.setTextSize(13);
		pieMenu.setOutlineColor(Color.BLACK, 225);
		pieMenu.setInnerRingColor(0xAA66CC, 180);
		pieMenu.setOuterRingColor(0x0099CC, 180);
//		pieMenu.setHeader("Set Menu", 20);
		pieMenu.setCenterCircle(menuCloseItem);

		pieMenu.addMenuEntry(new ArrayList<RadialMenuItem>() {
			{
				add(menuShowItem);
				add(menuCollectionItem);
				add(menuReportItem);
			}
		});
	}
	/****************************************************系统回调函数*************************************************************************/
	public void start_local_TimerTask(){
		//定时在chartview中显示实时数据波形以及无量纲指标数值
		if (updateCurDataInChartView_TimerTask!=null) {
			updateCurDataInChartView_TimerTask.cancel();
		}
		updateCurDataInChartView_TimerTask = new UpdateCurDataInChartView_TimerTask();
		timer.schedule(updateCurDataInChartView_TimerTask, 1000, GlobleVariable.UPDATE_CUR_DATA_PER_TIME);

		//定时更新诊断报告中的数据
		if (update_reportData!=null) {
			update_reportData.cancel();
		}
		update_reportData = new Update_reportData();
		timer_for_report.schedule(update_reportData, 1000, GlobleVariable.UPDATE_REPORT_PER_TIME);

	}
	public void start_web_TimerTask(){
		//定时在chartview中显示实时数据波形以及无量纲指标数值
		if (updateCurDataInChartViewFromWeb_timerTask!=null) {
			updateCurDataInChartViewFromWeb_timerTask.cancel();
		}
		updateCurDataInChartViewFromWeb_timerTask = new UpdateCurDataInChartViewFromWeb_TimerTask();
		timer.schedule(updateCurDataInChartViewFromWeb_timerTask, 1000, GlobleVariable.UPDATE_CUR_DATA_PER_TIME);

		//定时更新诊断报告中的数据
		if (update_reportWebData!=null) {
			update_reportWebData.cancel();
		}
		update_reportWebData = new Update_reportWebData();
		timer_for_report.schedule(update_reportWebData, 1000, GlobleVariable.UPDATE_REPORT_PER_TIME);
	}
	public void stop_all_TimerTask(){
		if (updateCurDataInChartView_TimerTask!=null) {
			updateCurDataInChartView_TimerTask.cancel();
		}
		if (update_reportData!=null) {
			update_reportData.cancel();
		}
		if (updateCurDataInChartViewFromWeb_timerTask!=null) {
			updateCurDataInChartViewFromWeb_timerTask.cancel();
		}
		if (update_reportWebData!=null) {
			update_reportWebData.cancel();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.webButton:
				classShowCurData.baseLine = sfv.getHeight()/2;
				//显示左上角麦克风绿色波形以及更新波形和无量纲指标
				classShowCurData.StartForWeb(recBufSize, sfv, mPaint);

//			start_web_TimerTask();

				sceneButton.setEnabled(false);
				sceneButton.setTextColor(Color.WHITE);
				stopButton.setEnabled(true);
				stopButton.setTextColor(Color.WHITE);
				webButton.setEnabled(false);
				sceneButton.setTextColor(Color.rgb(60, 66, 68));
				collctionTips.setText("采集中...");
				break;
			case R.id.startButton:
				classShowCurData.baseLine = sfv.getHeight()/2;
				//显示左上角麦克风绿色波形以及更新波形和无量纲指标
				classShowCurData.Start(audioRecord, recBufSize, sfv, mPaint);

				start_local_TimerTask();

				sceneButton.setEnabled(false);
				sceneButton.setTextColor(Color.rgb(60, 66, 68));
				stopButton.setEnabled(true);
				stopButton.setTextColor(Color.WHITE);
				webButton.setEnabled(false);
				sceneButton.setTextColor(Color.rgb(60, 66, 68));
				collctionTips.setText("采集中...");
				break;
			case R.id.stopButton:
				classShowCurData.Stop();

				stop_all_TimerTask();

				sceneButton.setEnabled(true);
				sceneButton.setTextColor(Color.WHITE);
				stopButton.setEnabled(false);
				stopButton.setTextColor(Color.rgb(60, 66, 68));
				webButton.setEnabled(true);
				sceneButton.setTextColor(Color.WHITE);
				collctionTips.setText("停止采集");
				break;
			case R.id.pause_and_start_imageBtn:
				if (ifPause_update==false) {
					pauseLabel.setText("开始更新");
					ifPause_update = true;
					pause_Btn.setImageDrawable(getResources().getDrawable(R.drawable.start));
				}else {
					pauseLabel.setText("暂停更新");
					ifPause_update = false;
					pause_Btn.setImageDrawable(getResources().getDrawable(R.drawable.pause));
				}

				pause_underLine.setBackgroundColor(Color.CYAN);
				set_underLine.setBackgroundColor(Color.TRANSPARENT);
				report_underLine.setBackgroundColor(Color.TRANSPARENT);
				break;
			case R.id.colletion_set_imageBtn:
				pieMenu.show(collction_set_Btn,0,250);

				pause_underLine.setBackgroundColor(Color.TRANSPARENT);
				set_underLine.setBackgroundColor(Color.CYAN);
				report_underLine.setBackgroundColor(Color.TRANSPARENT);
				break;
			case R.id.report_imageBtn:

				//侧滑菜单弹出
				mReportDrawer.toggleMenu();


				pause_underLine.setBackgroundColor(Color.TRANSPARENT);
				set_underLine.setBackgroundColor(Color.TRANSPARENT);
				report_underLine.setBackgroundColor(Color.CYAN);
				break;
			default:
				break;
		}
	}

	/**
	 * 在TabActivity中无法像普通Activity中监听返回键，都是无法执行操作的，只有使用该方法才能实现要求
	 * param event
	 * return
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN && event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			/*弹出对话框*/
			AlertDialog.Builder builder=new AlertDialog.Builder(context).setTitle("是否退出程序？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					CurData_Activity.this.finish();
					System.exit(0);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {

				}
			});
			builder.create();
			builder.show();
		}
		return super.dispatchKeyEvent(event);
	}

	/****************************************************其他地方需要用到的回调接口*************************************************************************/

	RestartTimerTask_CallBack restartTimerTask_CallBack = new RestartTimerTask_CallBack() {

		@Override
		public void restartTimerTask() {
			// TODO Auto-generated method stub
			start_local_TimerTask();
		}
	};
}
