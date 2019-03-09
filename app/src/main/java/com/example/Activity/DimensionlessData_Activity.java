package com.example.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.example.Activity.DimensionlessHistoryData_Activity.ReadDimensionlessdata_Thread;
import com.example.Adapter.MyPagerAdapter;
import com.example.FileUtils.ClassReadDimensionlessdata_From_HistoryFile;
import com.example.MyChart.TimeChart;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 无量纲指标显示界面
 * @author Administrator
 *
 */
public class DimensionlessData_Activity extends Activity implements View.OnTouchListener,View.OnClickListener,OnCheckedChangeListener{

	
	private Context context=this;
	
	/**
	 * 功能按钮
	 */
	private RadioButton recently_1minBtn=null;
	private RadioButton recently_5minBtn=null;
	private RadioButton custom_Btn=null;
	/**
	 * 按钮的下标志线
	 */
	private ImageView tabBtn1_underLine = null;
	private ImageView tabBtn2_underLine = null;
	private ImageView tabBtn3_underLine = null;
	/**
	 * 开始，停止更新按钮
	 */
	private ImageButton startBtn = null;
	private ImageButton stopBtn = null;
	
	/**
	 * 日期区间选择所需控件
	 */
	private EditText editStartTime;
	private EditText editEndTime;
	private Date startDate = null;//自定义起始区间
	private Date endDate = null;  //自定义结束区间
	private ImageButton searchButton = null;
	/**
	 * 时间段选择LinearLayout
	 */
	private LinearLayout chooseDateTimeLayout=null;
	/** 
     * 用于显示和隐藏时间段选择LinearLayout 
     */  
    private MarginLayoutParams marginLayoutParams;
    /** 
     * 隐藏的高度 
     */  
    private int hideHeight=0;private boolean hasMeasured = false; 
    /**
     * 动画
     */
    private Animation animationTranslate;
    
    /**
	 * 无量纲指标显示
	 */
	private LinearLayout mChartLayout1 = null;
	private LinearLayout mChartLayout2 = null;
	private LinearLayout mChartLayout3 = null;
	private LinearLayout mChartLayout4 = null;
	private LinearLayout mChartLayout5 = null;
	private LinearLayout mChartLayout6 = null;
	/**
	 * 图表将创建在该view上
	 */
	public GraphicalView mGraphicalView1=null;
	public GraphicalView mGraphicalView2=null;
	public GraphicalView mGraphicalView3=null;
	public GraphicalView mGraphicalView4=null;
	public GraphicalView mGraphicalView5=null;
	public GraphicalView mGraphicalView6=null;
	/**
	 * 时间图表渲染器
	 */
	private TimeChart timeChart1 = null;
	private TimeChart timeChart2 = null;
	private TimeChart timeChart3 = null;
	private TimeChart timeChart4 = null;
	private TimeChart timeChart5 = null;
	private TimeChart timeChart6 = null;
	/**
	 * 自定义时间段需要用到
	 */
	List<ArrayList<Float>> DimensionlessDatas = null;
	Date[] DimensionlessDates = null;
	
	/**
	 * 定时器，用于执行定时任务（刷新无量纲记录）
	 */
	private Timer timer = new Timer();
	private MyTimerTask mTimerTask;
	/**
	 * 是否正在更新标志
	 */
	private boolean isUpdating = false;
	
	public DimensionlessData_Activity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dimensionless_data_activity);
		
		initUI();
		Init_Layout();
	}

	private void initUI(){
		
		chooseDateTimeLayout = (LinearLayout)findViewById(R.id.chooseDateTime_LinerLayout);
		//获取该LinearLayout高度
		ViewTreeObserver vto = chooseDateTimeLayout.getViewTreeObserver();
		vto.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (hasMeasured == false) {
					hideHeight = chooseDateTimeLayout.getMeasuredHeight();
					hasMeasured = true;
					//隐藏
					marginLayoutParams=(MarginLayoutParams)chooseDateTimeLayout.getLayoutParams();
					marginLayoutParams.topMargin = -hideHeight;
					chooseDateTimeLayout.setLayoutParams(marginLayoutParams);
					recently_1minBtn.setChecked(true);
				}
				return true;
			}
		});
		
		recently_1minBtn=(RadioButton)findViewById(R.id.radio_recently1min);
		recently_1minBtn.setOnCheckedChangeListener(this);
		recently_5minBtn=(RadioButton)findViewById(R.id.radio_recently5min);
		recently_5minBtn.setOnCheckedChangeListener(this);
		custom_Btn=(RadioButton)findViewById(R.id.radio_customTime);
		custom_Btn.setOnCheckedChangeListener(this);
		
		tabBtn1_underLine = (ImageView)findViewById(R.id.tabButton1_underLine);
		tabBtn2_underLine = (ImageView)findViewById(R.id.tabButton2_underLine);
		tabBtn3_underLine = (ImageView)findViewById(R.id.tabButton3_underLine);
		
		startBtn = (ImageButton)findViewById(R.id.startButton);
		startBtn.setOnClickListener(this);
		stopBtn = (ImageButton)findViewById(R.id.stopButton);
		stopBtn.setOnClickListener(this);
		
		editStartTime = (EditText)this.findViewById(R.id.edit_start_time);
    	editStartTime.setOnTouchListener(this);
    	editEndTime = (EditText)this.findViewById(R.id.edit_end_time);
    	editEndTime.setOnTouchListener(this);
    	searchButton = (ImageButton)findViewById(R.id.search_Imagebutton);
    	searchButton.setOnClickListener(this);
    	
	}
	
	 private void Init_Layout(){
	    	mChartLayout1 = (LinearLayout)findViewById(R.id.LinearLayoutView1);
			timeChart1 = new TimeChart(Color.YELLOW, PointStyle.CIRCLE, "烈度指标值", "时刻点", "指标值");
			mChartLayout2 = (LinearLayout)findViewById(R.id.LinearLayoutView2);
			timeChart2 = new TimeChart(Color.GREEN, PointStyle.CIRCLE, "脉冲指标值", "时刻点", "指标值");
			mChartLayout3 = (LinearLayout)findViewById(R.id.LinearLayoutView3);
			timeChart3 = new TimeChart(Color.MAGENTA, PointStyle.CIRCLE, "欲度指标值", "时刻点", "指标值");
			mChartLayout4 = (LinearLayout)findViewById(R.id.LinearLayoutView4);
			timeChart4 = new TimeChart(Color.CYAN, PointStyle.CIRCLE, "峰值指标值", "时刻点", "指标值");
			mChartLayout5 = (LinearLayout)findViewById(R.id.LinearLayoutView5);
			timeChart5 = new TimeChart(Color.BLUE, PointStyle.CIRCLE, "峭度指标值", "时刻点", "指标值");
			mChartLayout6 = (LinearLayout)findViewById(R.id.LinearLayoutView6);
			timeChart6 = new TimeChart(Color.RED, PointStyle.CIRCLE, "波形指标值", "时刻点", "指标值");
	    }

	 /**
		 * 根据区间范围筛选文件并读取数据
		 * @author Administrator
		 *
		 */
		class ReadDimensionlessdata_Thread extends Thread{
			
			private Date startDate = null;
			private Date endDate = null;
			
			private int type = 0;
			
			ReadDimensionlessdata_Thread(Date startDate, Date endDate, int type){
				this.startDate = startDate;
				this.endDate = endDate;
				this.type = type;
			}
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ClassReadDimensionlessdata_From_HistoryFile aa = new ClassReadDimensionlessdata_From_HistoryFile(startDate, endDate,type);
				
				//自定义
				if (type == GlobleVariable.SHOW_CUSTOM) {
					long start = 0;
					long end = 0;
					if (startDate!=null && endDate!=null) {
						start = startDate.getTime();
						end = endDate.getTime();
						
						if(start < end){
							DimensionlessDatas = aa.getDimensionlessData_fromFile();
							DimensionlessDates = aa.getDates();
//							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//							for (int i = 0; i < DimensionlessDates.length; i++) {
//								System.out.println("date: "+simpleDateFormat.format(DimensionlessDates[i]));
//							}
							if(DimensionlessDatas!=null){
								createChartViewHandler.sendEmptyMessage(type);
							}else {
								createChartViewHandler.sendEmptyMessage(3);
							}
						}
					}
				} 
				//非自定义
				else {
					DimensionlessDatas = aa.getDimensionlessData_fromFile();
					if(DimensionlessDatas!=null){
						createChartViewHandler.sendEmptyMessage(type);//不同显示方式
					}else {
						createChartViewHandler.sendEmptyMessage(3);//无数据
					}
				}
			}
			
		}
		
		/**
		 * 创建图表
		 */
		Handler createChartViewHandler = new Handler(){

			@SuppressLint("HandlerLeak") @Override
			public void handleMessage(Message msg) { 
				// TODO Auto-generated method stub
				//有数据
				if (msg.what == GlobleVariable.SHOW_CUR_1MIN) {
					System.out.println("更新数据 最近一分钟！");
					create_ChartView(null,0);//显示最近一分钟
				}else if(msg.what == GlobleVariable.SHOW_CUR_5MIN){
					System.out.println("更新数据 最近五分钟！");
					create_ChartView(null,1);//显示最近五分钟
				}else if (msg.what == GlobleVariable.SHOW_CUSTOM) {
					System.out.println("更新数据 自定义时间段！");
					create_ChartView(DimensionlessDates,2);//显示自定义时间段
				}
				//空数据
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("温馨提示").setMessage("暂无该时间段数据！");
					//清空时间段变量
					startDate = null;
					endDate = null;
					builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
					builder.create();
					builder.show();
				}
			}
		};
		
		/**
		 * 创建各无量纲指标历史统计图
		 * @param dates
		 */
		private void create_ChartView(Date[] dates,int showType){
			for (int i = 0; i < DimensionlessDatas.size(); i++) {
				ArrayList<Float> tempArrayList = DimensionlessDatas.get(i);
				float[] floatdatas = change_Object_To_float(tempArrayList);
				init_ChartView(i, dates, floatdatas, showType);
			}
		}
		/**
		 * 将Object的List转换为float数组
		 * @param ArrayList
		 * @return
		 */
		private float[] change_Object_To_float(ArrayList<Float> ArrayList){
			float[] tempfloat = new float[ArrayList.size()];
			for (int i = 0; i < ArrayList.size(); i++) {
				tempfloat[i] = Float.parseFloat(ArrayList.get(i).toString());
			}
			return tempfloat;
		}
		private void init_ChartView(int num, Date[] dates, float[] floatdatas, int showType){
			
//			float[] datas = new float[dates.length];
//			for (int i = 0; i < datas.length; i++) {
//				datas[i] = (float)(new Random().nextFloat());
//			}
			
			switch (num) {
			case 0:
				XYMultipleSeriesDataset dataset1 = timeChart1.createDataset(floatdatas, dates, num, showType);
				XYMultipleSeriesRenderer renderer1 = timeChart1.createRenderer();
				mChartLayout1.removeAllViews();
				if (showType==GlobleVariable.SHOW_CUR_1MIN || showType==GlobleVariable.SHOW_CUR_5MIN) {
					fit(floatdatas, renderer1, showType);
					mGraphicalView1 = ChartFactory.getLineChartView(context, dataset1, renderer1);
				}else {
					mGraphicalView1 = ChartFactory.getTimeChartView(context, dataset1, renderer1, "M/d HH:mm:ss");
				}
				mChartLayout1.addView(mGraphicalView1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;
			case 1:
				XYMultipleSeriesDataset dataset2 = timeChart2.createDataset(floatdatas, dates, num, showType);
				XYMultipleSeriesRenderer renderer2 = timeChart2.createRenderer();
				mChartLayout2.removeAllViews();
				if (showType==GlobleVariable.SHOW_CUR_1MIN || showType==GlobleVariable.SHOW_CUR_5MIN) {
					fit(floatdatas, renderer2, showType);
					mGraphicalView2 = ChartFactory.getLineChartView(context, dataset2, renderer2);
				}else {
					mGraphicalView2 = ChartFactory.getTimeChartView(context, dataset2, renderer2, "M/d HH:mm:ss");
				}
				mChartLayout2.addView(mGraphicalView2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;
			case 2:
				XYMultipleSeriesDataset dataset3 = timeChart3.createDataset(floatdatas, dates, num, showType);
				XYMultipleSeriesRenderer renderer3 = timeChart3.createRenderer();
				mChartLayout3.removeAllViews();
				if (showType==GlobleVariable.SHOW_CUR_1MIN || showType==GlobleVariable.SHOW_CUR_5MIN) {
					fit(floatdatas, renderer3, showType);
					mGraphicalView3 = ChartFactory.getLineChartView(context, dataset3, renderer3);
				}
				else {
					mGraphicalView3 = ChartFactory.getTimeChartView(context, dataset3, renderer3, "M/d HH:mm:ss");
				}
				mChartLayout3.addView(mGraphicalView3, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;
			case 3:
				XYMultipleSeriesDataset dataset4 = timeChart4.createDataset(floatdatas, dates, num, showType);
				XYMultipleSeriesRenderer renderer4 = timeChart4.createRenderer();
				mChartLayout4.removeAllViews();
				if (showType==GlobleVariable.SHOW_CUR_1MIN || showType==GlobleVariable.SHOW_CUR_5MIN) {
					fit(floatdatas, renderer4, showType);
					mGraphicalView4 = ChartFactory.getLineChartView(context, dataset4, renderer4);
				}else {
					mGraphicalView4 = ChartFactory.getTimeChartView(context, dataset4, renderer4, "M/d HH:mm:ss");
				}
				mChartLayout4.addView(mGraphicalView4, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;
			case 4:
				XYMultipleSeriesDataset dataset5 = timeChart5.createDataset(floatdatas, dates, num, showType);
				XYMultipleSeriesRenderer renderer5 = timeChart5.createRenderer();
				mChartLayout5.removeAllViews();
				if (showType==GlobleVariable.SHOW_CUR_1MIN || showType==GlobleVariable.SHOW_CUR_5MIN) {
					fit(floatdatas, renderer5, showType);
					mGraphicalView5 = ChartFactory.getLineChartView(context, dataset5, renderer5);
				}else {
					mGraphicalView5 = ChartFactory.getTimeChartView(context, dataset5, renderer5, "M/d HH:mm:ss");
				}
				mChartLayout5.addView(mGraphicalView5, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;
			case 5:
				XYMultipleSeriesDataset dataset6 = timeChart6.createDataset(floatdatas, dates, num, showType);
				XYMultipleSeriesRenderer renderer6 = timeChart6.createRenderer();
				mChartLayout6.removeAllViews();
				if (showType==GlobleVariable.SHOW_CUR_1MIN || showType==GlobleVariable.SHOW_CUR_5MIN) {
					fit(floatdatas, renderer6, showType);
					mGraphicalView6 = ChartFactory.getLineChartView(context, dataset6, renderer6);
				}else {
					mGraphicalView6 = ChartFactory.getTimeChartView(context, dataset6, renderer6, "M/d HH:mm:ss");
				}
				mChartLayout6.addView(mGraphicalView6, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				break;
			default:
				break;
			}
		}
		
		/**
		 * 对图表进行一些自动适应
		 */
		private void fit(float[] data, XYMultipleSeriesRenderer mSeriiesXYRenderer, int timeType){
			float max=0;
			for (int i = 0; i < data.length; i++) {
				if(data[i]>max)
					max = data[i];
			}
			//根据当前情况对渲染器做一点修改(只能上下移动，不可放大)
			mSeriiesXYRenderer.setPanEnabled(true, true);
			mSeriiesXYRenderer.setZoomEnabled(false);
			switch (timeType) {
			case GlobleVariable.SHOW_CUR_1MIN:
				mSeriiesXYRenderer.setXAxisMax(60);
				mSeriiesXYRenderer.setXLabels(30);
				mSeriiesXYRenderer.setXTitle("时间/秒");
				break;
			case GlobleVariable.SHOW_CUR_5MIN:
				mSeriiesXYRenderer.setXAxisMax(300);
				mSeriiesXYRenderer.setXLabels(30);
				mSeriiesXYRenderer.setXTitle("时间/秒");
				break;
			case GlobleVariable.SHOW_CUSTOM:
				break;
			default:
				break;
			}
			//自适应y轴
			mSeriiesXYRenderer.setYAxisMin(-(Math.ceil(max/10)*10));
			mSeriiesXYRenderer.setYAxisMax(Math.ceil(max/10)*10);
//			mSeriiesXYRenderer.setYAxisMin(-10);
//			mSeriiesXYRenderer.setYAxisMax(10);
			mSeriiesXYRenderer.setYLabels(16);
//			//自适应X轴
//			mSeriiesXYRenderer.setXAxisMax(data.length);
		}

		
		/**
		 * 开始更新无量纲(用于先点击染radioButton,再点击更新按钮)
		 * @param type  要跟新的数据时间类型（最近1分钟，最近5分钟，自定义时间段）
		 */
		private void updateStart(){
			//如果原任务不为空，先将原任务从队列中移除，不然会出错（一个定时器只能添加一个任务）
			if(mTimerTask!=null)
				mTimerTask.cancel();
			
			if (recently_1minBtn.isChecked()) {
				mTimerTask = new MyTimerTask(GlobleVariable.SHOW_CUR_1MIN);
				timer.schedule(mTimerTask, 1000, GlobleVariable.UPDATE_DIMENSIONLESS_PER_TIME);
			}else if(recently_5minBtn.isChecked()){
				mTimerTask = new MyTimerTask(GlobleVariable.SHOW_CUR_5MIN);
				timer.schedule(mTimerTask, 1000, GlobleVariable.UPDATE_DIMENSIONLESS_PER_TIME);
			}
			//由于自定义时间段显示需要在点击搜索按钮后才显示，因此不能放在这里，放在这里不用点击搜索按钮亦可以触发更新自定义时间段数据！
//			else if (custom_Btn.isChecked()) {
//				mTimerTask = new MyTimerTask(GlobleVariable.SHOW_CUSTOM);
//				timer.schedule(mTimerTask, 1000, GlobleVariable.UPDATE_PER_TIME);
//			}
			isUpdating = true;
		}
		/**
		 * 开始更新无量纲(用于先点击更新按钮，再点击radioButton)
		 * @param type  要跟新的数据时间类型（最近1分钟，最近5分钟，自定义时间段）
		 */
		private void updateStart_ifUpdatBtn_Have_Press(int type){
			//如果原任务不为空，先将原任务从队列中移除，不然会出错（一个定时器只能添加一个任务）
			if(mTimerTask!=null)
				mTimerTask.cancel();
			
			switch (type) {
			case GlobleVariable.SHOW_CUR_1MIN:
				mTimerTask = new MyTimerTask(GlobleVariable.SHOW_CUR_1MIN);
				timer.schedule(mTimerTask, 1000, GlobleVariable.UPDATE_DIMENSIONLESS_PER_TIME);
				break;
			case GlobleVariable.SHOW_CUR_5MIN:
				mTimerTask = new MyTimerTask(GlobleVariable.SHOW_CUR_5MIN);
				timer.schedule(mTimerTask, 1000, GlobleVariable.UPDATE_DIMENSIONLESS_PER_TIME);
				break;
			case GlobleVariable.SHOW_CUSTOM:
				mTimerTask = new MyTimerTask(GlobleVariable.SHOW_CUSTOM);
				timer.schedule(mTimerTask, 1000, GlobleVariable.UPDATE_DIMENSIONLESS_PER_TIME);
				break;
			default:
				break;
			}
			isUpdating = true;
		}
		/**
		 * 停止更新无量纲
		 */
		private void updateStop(){
			if(mTimerTask!=null)
				mTimerTask.cancel();
			
			isUpdating = false;
		}
	 
		/**
		 * 自定义定时任务类
		 * @author Administrator
		 *
		 */
		class MyTimerTask extends TimerTask{
			private int type = 0;
			
			public MyTimerTask(int type){
				this.type = type;
			}
			
			@Override
			public void run() {
					switch (type) {
					case GlobleVariable.SHOW_CUR_1MIN:
						new ReadDimensionlessdata_Thread(null, null,GlobleVariable.SHOW_CUR_1MIN).start();
						break;
					case GlobleVariable.SHOW_CUR_5MIN:
						new ReadDimensionlessdata_Thread(null, null,GlobleVariable.SHOW_CUR_5MIN).start();
						break;
					case GlobleVariable.SHOW_CUSTOM:
						new ReadDimensionlessdata_Thread(startDate, endDate,GlobleVariable.SHOW_CUSTOM).start();
						break;
					default:
						break;
					}
			}
		}

	/**
	 * 弹出日期时间选择
	 */
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (view.getId()==R.id.edit_start_time || view.getId()==R.id.edit_end_time) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				View v = View.inflate(context, R.layout.date_time_dialog, null);
				builder.setView(v);
				final DatePicker datePicker = (DatePicker)v.findViewById(R.id.datePicker);
				final TimePicker timePicker = (TimePicker)v.findViewById(R.id.timePicker);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
				
				timePicker.setIs24HourView(true);
				timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
				
				if (view.getId() == R.id.edit_start_time) {
					int inType = editStartTime.getInputType();
					editStartTime.setInputType(InputType.TYPE_NULL);
					editStartTime.onTouchEvent(event);
					editStartTime.setInputType(inType);
					editStartTime.setSelection(editStartTime.getText().length());
					
					builder.setTitle("选取起始时间");
					builder.setPositiveButton("确   定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							StringBuffer sb = new StringBuffer();
							sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth()));
							sb.append("   ");
//							sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
							sb.append(String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
							editStartTime.setText(sb);
							editEndTime.requestFocus();
							dialog.cancel();
							String startTime = editStartTime.getText().toString();
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							try {
								startDate = simpleDateFormat.parse(startTime);
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
				}else if (view.getId() == R.id.edit_end_time) {
					int inType = editEndTime.getInputType();
					editEndTime.setInputType(InputType.TYPE_NULL);
					editEndTime.onTouchEvent(event);
					editEndTime.setInputType(inType);
					editEndTime.setSelection(editEndTime.getText().length());
					
					builder.setTitle("选取结束时间");
					builder.setPositiveButton("确   定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							StringBuffer sb = new StringBuffer();
							sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth()));
							sb.append("   "); 
//							sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
							sb.append(String.format("%02d:%02d", timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
							editEndTime.setText(sb);
							dialog.cancel();
							String endTime = editEndTime.getText().toString();
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							try {
								endDate = simpleDateFormat.parse(endTime);
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					});
				}
				Dialog dialog = builder.create();
				dialog.show();
			}
		}
		
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		//点击搜索按钮
		case R.id.search_Imagebutton:
			if (startDate!=null && endDate!=null) {
				new ReadDimensionlessdata_Thread(startDate, endDate,GlobleVariable.SHOW_CUSTOM).start();
				if (isUpdating) {
//					updateStart();
					updateStart_ifUpdatBtn_Have_Press(GlobleVariable.SHOW_CUSTOM);
				}else {
					updateStop();
				}
			}
			break;
		//开始更新按钮
		case R.id.startButton:
			updateStart();
//			startBtn.setImageResource(R.drawable.startupdate_click);
			startBtn.setEnabled(false);
//			stopBtn.setImageResource(R.drawable.stopupdate);
			stopBtn.setEnabled(true);
			break;
		//停止更新按钮
		case R.id.stopButton:
			updateStop();
//			startBtn.setImageResource(R.drawable.startupdate);
			startBtn.setEnabled(true);
//			stopBtn.setImageResource(R.drawable.stopupdate_click);
			stopBtn.setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton radionButton, boolean isChecked) {
		if (isChecked) {
			switch (radionButton.getId()) {
			//点击最近一分钟
			case R.id.radio_recently1min:
				tabBtn1_underLine.setBackgroundColor(Color.CYAN);
				tabBtn2_underLine.setBackgroundColor(Color.TRANSPARENT);
				tabBtn3_underLine.setBackgroundColor(Color.TRANSPARENT);

				System.out.println("1min checked");
				if (marginLayoutParams.topMargin==0) {
					//收起时间段选择
					if (marginLayoutParams.topMargin==0) {
						marginLayoutParams.topMargin = -hideHeight;
						chooseDateTimeLayout.setLayoutParams(marginLayoutParams);
					}
//					chooseDateTimeLayout.startAnimation(animTranslate(0,-hideHeight,0,-hideHeight,chooseDateTimeLayout,500));
				}
				new ReadDimensionlessdata_Thread(null, null,GlobleVariable.SHOW_CUR_1MIN).start();
				if (isUpdating) {
//					updateStart();
					updateStart_ifUpdatBtn_Have_Press(GlobleVariable.SHOW_CUR_1MIN);
				}else {
					updateStop();
				}
				break;
			//点击最近五分钟
			case R.id.radio_recently5min:
				tabBtn1_underLine.setBackgroundColor(Color.TRANSPARENT);
				tabBtn2_underLine.setBackgroundColor(Color.CYAN);
				tabBtn3_underLine.setBackgroundColor(Color.TRANSPARENT);

				System.out.println("5min checked");
				if (marginLayoutParams.topMargin==0) {
					//收起时间段选择
					if (marginLayoutParams.topMargin==0) {
						marginLayoutParams.topMargin = -hideHeight;
						chooseDateTimeLayout.setLayoutParams(marginLayoutParams);
					}
//					chooseDateTimeLayout.startAnimation(animTranslate(0,-hideHeight,0,-hideHeight,chooseDateTimeLayout,500));
				}
				new ReadDimensionlessdata_Thread(null, null,GlobleVariable.SHOW_CUR_5MIN).start();
				//先点击更新按钮，再点击radioButton
				if (isUpdating) {
//					updateStart();
					updateStart_ifUpdatBtn_Have_Press(GlobleVariable.SHOW_CUR_5MIN);
				}else {
					updateStop();
				}
				break;
			//点击自定义时间段
			case R.id.radio_customTime:
				tabBtn1_underLine.setBackgroundColor(Color.TRANSPARENT);
				tabBtn2_underLine.setBackgroundColor(Color.TRANSPARENT);
				tabBtn3_underLine.setBackgroundColor(Color.CYAN);
				
				System.out.println("custom checked");
				if (marginLayoutParams.topMargin<0) {
					marginLayoutParams.topMargin = 0;
					chooseDateTimeLayout.setLayoutParams(marginLayoutParams);
				}
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 在TabActivity中无法像普通Activity中监听返回键，都是无法执行操作的，只有使用该方法才能实现要求
	 * @param event
	 * @return
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getAction()==KeyEvent.ACTION_DOWN && event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			/*弹出对话框*/
			AlertDialog.Builder builder=new AlertDialog.Builder(context).setTitle("是否退出程序？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					DimensionlessData_Activity.this.finish();
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
	
	/**
	 * 移动的动画效果
	 */
	protected Animation animTranslate(int toX, int toY, final int lastX, final int lastY,final LinearLayout linearLayout, long durationMillis) 
	{
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);				
		animationTranslate.setAnimationListener(new AnimationListener()
		{
						
			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub
								
			}
						
			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				// TODO Auto-generated method stub
							
			}
						
			@Override
			public void onAnimationEnd(Animation animation)
			{
				//这下面几条语句是重新设置Button在父控件里的布局，因为如果只是动画效果过去，控件所触发监听事件的位置还是原来的位置
				linearLayout.clearAnimation();//清除动画效果
				marginLayoutParams.topMargin=lastY;
				linearLayout.setLayoutParams(marginLayoutParams);
			}
		});																								
		animationTranslate.setDuration(durationMillis);
		animationTranslate.setFillAfter(true);
		return animationTranslate;
	}
}
