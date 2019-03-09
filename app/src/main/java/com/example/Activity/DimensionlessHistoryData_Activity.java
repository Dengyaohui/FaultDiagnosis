package com.example.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.example.FileUtils.ClassReadDimensionlessdata_From_HistoryFile;
import com.example.MyChart.TimeChart;

import android.text.InputType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;
/**
 * 无量纲指标显示中根据自定义时间显示数据
 * @author Administrator
 *
 */
public class DimensionlessHistoryData_Activity extends Activity implements View.OnTouchListener,View.OnClickListener {

	private Context context = this;
	/**
	 * 日期区间选择所需控件
	 */
	private EditText editStartTime;
	private EditText editEndTime;
	private Date startDate = null;
	private Date endDate = null;
	private ImageButton searchButton = null;
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
	
	List<ArrayList<Float>> DimensionlessDatas = null;
	Date[] DimensionlessDates = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dimensionless_history_activity);
        
        Init_UI();
        Init_Layout();
    }

    private void Init_UI(){
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
    
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
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
//						sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
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
//						sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
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
		
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view.getId() == R.id.search_Imagebutton) {
			if (startDate!=null && endDate!=null) {
				new ReadDimensionlessdata_Thread(startDate, endDate).start();
			}
		}
		
	}

	/**
	 * 根据区间范围筛选文件并读取数据
	 * @author Administrator
	 *
	 */
	class ReadDimensionlessdata_Thread extends Thread{
		
		private Date startDate = null;
		private Date endDate = null;
		
		ReadDimensionlessdata_Thread(Date startDate, Date endDate){
			this.startDate = startDate;
			this.endDate = endDate;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			long start = startDate.getTime();
			long end = endDate.getTime();
			if(start < end){
				ClassReadDimensionlessdata_From_HistoryFile aa = new ClassReadDimensionlessdata_From_HistoryFile(startDate, endDate,GlobleVariable.SHOW_CUSTOM);
				
				DimensionlessDatas = aa.getDimensionlessData_fromFile();
				DimensionlessDates = aa.getDates();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				for (int i = 0; i < DimensionlessDates.length; i++) {
//					System.out.println("date: "+simpleDateFormat.format(DimensionlessDates[i]));
//				}
				
				if(DimensionlessDatas!=null){
					createChartViewHandler.sendEmptyMessage(0);
				}else {
					createChartViewHandler.sendEmptyMessage(1);
				}
			} 
		}
		
	}
	
	/**
	 * 创建图表
	 */
	Handler createChartViewHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) { 
			// TODO Auto-generated method stub
			//有数据
			if (msg.what == 0) {
//				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				long start = startDate.getTime();
//				long end = endDate.getTime();
//				
//				int dataLength = DimensionlessDatas.get(0).size();
//				
//				long date = (long)((end-start)/dataLength);
//				Date[] dates = new Date[dataLength];
//				
//				for (int i = 0; i < dataLength && start<end; i++) {
//					if(i==0){
//						dates[i] = new Date(start);
//					}else if (i==dataLength-1) {
//						dates[i] = new Date(end);
//					}
//					else {
//						dates[i] = new Date(start+date);
//						start = start+date;
//					}
//				}
				
				create_ChartView(DimensionlessDates);
			}
			//空数据
			else if (msg.what == 1) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("温馨提示").setMessage("暂无该时间段数据！");
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
	private void create_ChartView(Date[] dates){
		for (int i = 0; i < DimensionlessDatas.size(); i++) {
			ArrayList<Float> tempArrayList = DimensionlessDatas.get(i);
			float[] floatdatas = change_Object_To_float(tempArrayList);
			init_ChartView(i, dates, floatdatas);
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
	private void init_ChartView(int num, Date[] dates, float[] floatdatas){
		
		float[] datas = new float[dates.length];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = (float)(new Random().nextFloat());
		}
		
		switch (num) {
		case 0:
			XYMultipleSeriesDataset dataset1 = timeChart1.createDataset(floatdatas, dates, num,GlobleVariable.SHOW_CUSTOM);
			XYMultipleSeriesRenderer renderer1 = timeChart1.createRenderer();
			mChartLayout1.removeAllViews();
			mGraphicalView1 = ChartFactory.getTimeChartView(context, dataset1, renderer1, "M/d HH:mm:ss");
			mChartLayout1.addView(mGraphicalView1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			break;
		case 1:
			XYMultipleSeriesDataset dataset2 = timeChart2.createDataset(floatdatas, dates, num,GlobleVariable.SHOW_CUSTOM);
			XYMultipleSeriesRenderer renderer2 = timeChart2.createRenderer();
			mChartLayout2.removeAllViews();
			mGraphicalView2 = ChartFactory.getTimeChartView(context, dataset2, renderer2, "M/d HH:mm:ss");
			mChartLayout2.addView(mGraphicalView2, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			break;
		case 2:
			XYMultipleSeriesDataset dataset3 = timeChart3.createDataset(floatdatas, dates, num,GlobleVariable.SHOW_CUSTOM);
			XYMultipleSeriesRenderer renderer3 = timeChart3.createRenderer();
			mChartLayout3.removeAllViews();
			mGraphicalView3 = ChartFactory.getTimeChartView(context, dataset3, renderer3, "M/d HH:mm:ss");
			mChartLayout3.addView(mGraphicalView3, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			break;
		case 3:
			XYMultipleSeriesDataset dataset4 = timeChart4.createDataset(floatdatas, dates, num,GlobleVariable.SHOW_CUSTOM);
			XYMultipleSeriesRenderer renderer4 = timeChart4.createRenderer();
			mChartLayout4.removeAllViews();
			mGraphicalView4 = ChartFactory.getTimeChartView(context, dataset4, renderer4, "M/d HH:mm:ss");
			mChartLayout4.addView(mGraphicalView4, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			break;
		case 4:
			XYMultipleSeriesDataset dataset5 = timeChart5.createDataset(floatdatas, dates, num,GlobleVariable.SHOW_CUSTOM);
			XYMultipleSeriesRenderer renderer5 = timeChart5.createRenderer();
			mChartLayout5.removeAllViews();
			mGraphicalView5 = ChartFactory.getTimeChartView(context, dataset5, renderer5, "M/d HH:mm:ss");
			mChartLayout5.addView(mGraphicalView5, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			break;
		case 5:
			XYMultipleSeriesDataset dataset6 = timeChart6.createDataset(floatdatas, dates, num,GlobleVariable.SHOW_CUSTOM);
			XYMultipleSeriesRenderer renderer6 = timeChart6.createRenderer();
			mChartLayout6.removeAllViews();
			mGraphicalView6 = ChartFactory.getTimeChartView(context, dataset6, renderer6, "M/d HH:mm:ss");
			mChartLayout6.addView(mGraphicalView6, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			break;
		default:
			break;
		}
	}
}
