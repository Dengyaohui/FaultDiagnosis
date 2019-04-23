package com.example.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapter.FileListViewAdapter;
import com.example.FileUtils.Class_SortFileAtModifiedTime;
import com.example.MyChart.TimeChart;
import com.example.MyUtilClass.Util;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 显示历史数据列表（用于导入数据）
 *
 * @author Administrator
 */
public class ImportDataFromFile_Activity extends Activity implements OnClickListener {

	private Context context = this;

	/**
	 * 弹出和隐藏MenuDrawer
	 */
	private ImageView show_and_hide_view = null;
	private TextView mCurrentPath;
	private TextView mReturn;
	private ListView mFileList;
	private View mPathLine;
	private String mReturnPath;
	private String nextPath;
	private String curPath;
	private String curDir;

	private FileListViewAdapter adapter;
	//	private ArrayList<Map<String, Object>> infos = null;
	private LinkedList<Map<String, Object>> infos = null;

	private ArrayList<Map<String, Object>> fileListInfo = new ArrayList<>();

	SurfaceView sfv;
	Paint mPaint;
	private TextView historyDateTextView = null;
	private String historyFileDate = null;
	private ImageButton showButton = null;
	private ImageButton lastDataButton = null;
	private ImageButton nextDataButton = null;
	private TextView collection_pointTextView = null;
	private ListView originalDataListView = null;

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
	 * 滑动显示窗口类
	 */
	private MenuDrawer mShowDataDrawer;
	/**
	 * 用于保存当前数据
	 */
	private float[] curData;
	/**
	 * 用于保存当前无量纲指标数据
	 */
	private float[] curDimensionlissData;
	/**
	 * 时间图表渲染器
	 */
	private TimeChart timeChart = null;
	private LinearLayout mChartLayout = null;
	public GraphicalView mGraphicalView = null;

	/**
	 * 工具类
	 * */
	private Util util;

//	private int width = 0,height=0;private boolean hasMeasured = false;

	//获取根目录
	String sdDir = String.valueOf(Environment.getExternalStorageDirectory());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);



		/*给当前Activity贴上滑动窗口,方向为...*/
		mShowDataDrawer.setSet_mMenuSize(450);                                           //设置drawer大小
		mShowDataDrawer = MenuDrawer.attach(this, Position.TOP);
		mShowDataDrawer.setContentView(R.layout.activity_show_history_filelist);         //其实函数内是为该activity设置布局的
		mShowDataDrawer.setMenuView(R.layout.import_data_activity);

		initView();
		initUI();

	}

	/****************************************************初始化*************************************************************************/

	private void initView() {
		show_and_hide_view = findViewById(R.id.show_and_hide_view);
		show_and_hide_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mShowDataDrawer.toggleMenu();
			}
		});

		mCurrentPath = (TextView) findViewById(R.id.file_Path);
		mPathLine = findViewById(R.id.file_Path_Line);
		mReturn = (TextView) findViewById(R.id.file_Return);
		mFileList = (ListView) findViewById(R.id.file_List);

		mFileList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				// TODO Auto-generated method stub
				File file = new File((String) infos.get(position).get("path"));
				//点击的是文件夹，进入下一级目录
				if (file.isDirectory()) {
					nextPath = (String) (infos.get(position).get("path"));
					initList(nextPath);
				}
				//点击的是文件，使用文件名执行相应操作(转到显示波形Activity)
				else {
					String fileName = (String) (infos.get(position).get("path"));
					if (fileName.endsWith(".pcm")) {
						mShowDataDrawer.toggleMenu();
						curPath = fileName;                    //保存当前点击的文件路径
						curDir = new File(curPath).getParent();//保存当前文件所在目录的路径
						//创建图表
						new Thread(new readData_From_File(fileName.substring(0, fileName.lastIndexOf(".")))).start();//传进去掉后缀名的文件名
					}
				}
			}
		});

		mReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				String returnStr = mReturn.getText().toString();
				if (mReturnPath.length() > 0 && returnStr.equals("返回上一级")) {
					initList(mReturnPath);
				}
			}
		});
		//打开根目录
		initList(sdDir);
	}

	private void initList(String path) {
		File file = new File(path);
		File[] filesList = file.listFiles();
		String[] stringsfilesList = file.list();
		Log.e("listFiles", Arrays.toString(file.listFiles()));
		//重新初始化一个
		infos = new LinkedList<>();
		Map<String, Object> item;
		Drawable drawable;
		//当前为根目录，返回上一级按钮不显示
		if (path.equals(sdDir)) {
//			drawable = getResources().getDrawable(R.drawable.mainbutton_collete);
//			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//			mCurrentPath.setCompoundDrawablePadding(10);
//			mCurrentPath.setCompoundDrawables(drawable, null, null, null);
			mCurrentPath.setText("根目录列表");
			mReturn.setVisibility(View.GONE);
			mPathLine.setVisibility(View.GONE);
		} else {
//			drawable = getResources().getDrawable(R.drawable.mainbutton_colleteset);
//			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//			mReturn.setCompoundDrawables(drawable, null, null, null);
			mReturn.setText("返回上一级");
			//保存该级目录的上一级路径
			mReturnPath = file.getParent();
			mCurrentPath.setText(file.getPath());
			mCurrentPath.setVisibility(View.VISIBLE);
			mReturn.setVisibility(View.VISIBLE);
			mPathLine.setVisibility(View.VISIBLE);
		}

		try {
			//先将filesList按修改时间排序(如果当前为故障诊断路径，就不需要排序，因为原始数据文件夹与无量纲指标文件夹修改时间几乎相等，使用这种方法会不显示其中一个文件夹!!!)
			File[] files;
			if (path.substring(path.lastIndexOf("/") + 1).equals(GlobleVariable.DATA_SAVE_DIR)) {
				files = filesList;
			} else {
				Class_SortFileAtModifiedTime sortFiles = new Class_SortFileAtModifiedTime(filesList);
				files = sortFiles.getSortFiles();
			}

			for (int i = 0; i < files.length; i++) {
				item = new HashMap<>();
				File fileItem = files[i];
				//根据文件夹与文件设置不同图表
				if (fileItem.isDirectory()) {
					item.put("icon", R.drawable.dir_logo);
				} else {
					item.put("icon", R.drawable.file_logo);
				}
				item.put("name", fileItem.getName());
				item.put("path", fileItem.getAbsolutePath());
				infos.add(item);
			}
		} catch (Exception e) {
			System.out.println("add infos Error------>" + e.toString());
		}
		adapter = new FileListViewAdapter(context);
		adapter.setFileListInfo(infos);
		mFileList.setAdapter(adapter);

	}

	private void initUI() {

		//画板和画笔
		sfv = findViewById(R.id.historySurfaceView);
		mPaint = new Paint();
		mPaint.setColor(Color.rgb(218, 112, 214));
		mPaint.setStrokeWidth(3);

		showButton = findViewById(R.id.showWaveBtn);
		showButton.setOnClickListener(this);
		lastDataButton = findViewById(R.id.lastDataBtn);
		lastDataButton.setOnClickListener(this);
		nextDataButton = findViewById(R.id.nextDataBtn);
		nextDataButton.setOnClickListener(this);

		historyDateTextView = findViewById(R.id.history_date_TextView);
		collection_pointTextView = findViewById(R.id.collection_point);
		originalDataListView = findViewById(R.id.originalDataListView);

		dataValueTextView1 = findViewById(R.id.bottomData1);
		dataValueTextView2 = findViewById(R.id.bottomData2);
		dataValueTextView3 = findViewById(R.id.bottomData3);
		dataValueTextView4 = findViewById(R.id.bottomData4);
		dataValueTextView5 = findViewById(R.id.bottomData5);
		dataValueTextView6 = findViewById(R.id.bottomData6);

		mChartLayout = findViewById(R.id.historyChartView);
		timeChart = new TimeChart(Color.CYAN, PointStyle.CIRCLE, "实时采样值", "采样点", "采样值");

	}

	/**
	 * 初始化原始数据列表
	 *
	 * @param historyData
	 */
	private void initOriginalListView(final float[] historyData) {
		if (historyData != null) {
			List<HashMap<String, String>> originalDatas = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < historyData.length; i++) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("point", String.valueOf(i));
				data.put("originalData", String.valueOf(historyData[i]));
				originalDatas.add(data);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(context, originalDatas, R.layout.import_originaldata_listitem,
					new String[]{"point", "originalData"}, new int[]{R.id.collection_point, R.id.originalDataListView_textView});
			originalDataListView.setAdapter(simpleAdapter);
			originalDataListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
										long arg3) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	/****************************************************创建图表*************************************************************************/
	/**
	 * 从文件中读取原始数据和无量纲指标数据
	 *
	 * @author Administrator
	 */
	class readData_From_File implements Runnable {
		private String importFileName;

		/**
		 * 构造函数
		 *
		 * @param fileName 点击文件名并去掉.txt后缀，因为读取无量纲要补.pcm
		 */
		public readData_From_File(String fileName) {
			this.importFileName = fileName;//去掉.txt后缀的点击文件名
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			curData = getImportData(importFileName + ".pcm");

			String dimensionlessDataFile = null;

			//判断需要使用哪个文件夹的文件名
			if (importFileName.substring(importFileName.lastIndexOf(GlobleVariable.DATA_SAVE_DIR)).equals(
					GlobleVariable.DATA_SAVE_DIR + "/" + GlobleVariable.CUR_ORIGINAL_DATA_PER_1S + importFileName.substring(importFileName.lastIndexOf("/")))) {
				dimensionlessDataFile = GlobleVariable.SD_CARD_PATH + "/" + GlobleVariable.DATA_SAVE_DIR + "/" + GlobleVariable.CUR_DIMENSION_LESS_PER_1S + "/" + importFileName.substring(importFileName.lastIndexOf("/")) + ".txt";
			} else if (importFileName.substring(importFileName.lastIndexOf(GlobleVariable.DATA_SAVE_DIR)).equals(
					GlobleVariable.DATA_SAVE_DIR + "/" + GlobleVariable.CUR_ORIGINAL_DATA_PER_5MIN + importFileName.substring(importFileName.lastIndexOf("/")))) {
				dimensionlessDataFile = GlobleVariable.SD_CARD_PATH + "/" + GlobleVariable.DATA_SAVE_DIR + "/" + GlobleVariable.CUR_DIMENSION_LESS_PER_5MIN + "/" + importFileName.substring(importFileName.lastIndexOf("/")) + ".txt";

			}
			curDimensionlissData = getDimensionlessData(dimensionlessDataFile);

			if (curData != null && curDimensionlissData != null) {
				CreateChartViewHandler.sendEmptyMessage(0);
			}
		}

	}

	Handler CreateChartViewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			createView();
		}

	};

	private void createView() {
		//在列表中把数据也显示出来 
		initOriginalListView(curData);

		XYMultipleSeriesDataset dataset = timeChart.createDataset(curData, "导入的数据波形");
		XYMultipleSeriesRenderer renderer = timeChart.createRenderer();

		fit(curData, renderer);

		mChartLayout.removeAllViews();
		mGraphicalView = ChartFactory.getLineChartView(context, dataset, renderer);
		mChartLayout.addView(mGraphicalView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		dataValueTextView1.setText(String.valueOf(curDimensionlissData[0]));
		dataValueTextView2.setText(String.valueOf(curDimensionlissData[1]));
		dataValueTextView3.setText(String.valueOf(curDimensionlissData[2]));
		dataValueTextView4.setText(String.valueOf(curDimensionlissData[3]));
		dataValueTextView5.setText(String.valueOf(curDimensionlissData[4]));
		dataValueTextView6.setText(String.valueOf(curDimensionlissData[5]));

		historyDateTextView.setText(historyFileDate);
	}

	/**
	 * 自适应坐标轴
	 */
	private void fit(float[] data, XYMultipleSeriesRenderer mSeriiesXYRenderer) {
		Float max = Float.valueOf(0);
		for (int i = 0; i < data.length; i++) {
			if (Math.abs(data[i]) > max)
				max = data[i];
		}
		//y轴
		mSeriiesXYRenderer.setYAxisMin(-(Math.ceil(max / 10) * 10));
		mSeriiesXYRenderer.setYAxisMax(Math.ceil(max / 10) * 10);
		mSeriiesXYRenderer.setYLabels(30);
		//x轴
		mSeriiesXYRenderer.setXAxisMax(data.length);
		mSeriiesXYRenderer.setXLabels(data.length / 10);
		//根据当前情况对渲染器做一点修改
		mSeriiesXYRenderer.setPanLimits(new double[]{0, data.length + data.length / 10, -(max * 2), max * 2});
		mSeriiesXYRenderer.setZoomEnabled(true);                          //是否显示放大缩小按钮
		mSeriiesXYRenderer.setPanEnabled(true, true);                     //是否左右上下可移动
	}

	/****************************************************获取数据*************************************************************************/
	/**
	 * 获取当前文件夹下所有文件信息到fileListInfo中
	 *
	 * @param curDirPath
	 */
	private void getCurAllFile(String curDirPath) {
		fileListInfo = new ArrayList<>();
		File curDir = new File(curDirPath);
		File[] files = curDir.listFiles();
		//按时间顺序排序
		Class_SortFileAtModifiedTime sortFiles = new Class_SortFileAtModifiedTime(files);
		files = null;
		files = sortFiles.getSortFiles();
		for (int i = 0; i < files.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fileName", files[i].getName());
			fileListInfo.add(map);
		}
	}

	/**
	 * 读取文件获取原始数据保存到historyBuf中
	 *
	 * @param fileName
	 */
	private float[] getImportData(String fileName) {
		File historyFile = new File(fileName);

		//保存当前路径所有文件名
		getCurAllFile(historyFile.getParent());

		//写进文件的数据是16位的，因为有正负，所以创建历史记录缓存区时长度为一半
		int length = (int) (historyFile.length() / 2);
		float[] historyData = new float[length];

		try {
			InputStream inputStream = new FileInputStream(historyFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

			int i = 0;
			while (dataInputStream.available() > 0) {
				historyData[i] = dataInputStream.readShort();
				i++;
			}
			dataInputStream.close();

		} catch (Exception e) {
			System.out.println("get historyData error----->" + e.toString());
		}

		//修改历史记录当时的时间
		String stringName = historyFile.getName();
		stringName = stringName.substring(0, 19);
		char[] historyTime = stringName.toCharArray();
		historyTime[10] = ' ';
		historyTime[13] = ':';
		historyTime[16] = ':';
		historyFileDate = String.valueOf(historyTime);
		return historyData;
	}

	private float[] getDimensionlessData(String fileName) {
		File historyFile = null;
		try {
			historyFile = new File(fileName);
		} catch (Exception e) {
			System.out.println("打开文件：" + e.toString());
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
			while (dataInputStream.available() > 0) {
				curDimensionlessData[i] = dataInputStream.readFloat();
				i++;
			}
		} catch (Exception e) {
			System.out.println("获取最新一秒无量纲指标数据失败----->" + e.toString());
		} finally {
			try {
				if (dataInputStream != null) {
					dataInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return curDimensionlessData;
	}
	/****************************************************系统回调函数*************************************************************************/
	/**
	 * 显示上一个数据
	 */
	private void showLastData() {
		int curPosition = 0;

		for (int i = 0; i < fileListInfo.size(); i++) {
			//要比较必须先补全路径名
			if (curPath.equals(curDir + "/" + fileListInfo.get(i).get("fileName").toString())) {
				curPosition = i;
			}
		}
		if (curPosition == 0) {
			Toast.makeText(context, "已无上一个数据!", Toast.LENGTH_SHORT).show();
		} else {
			//设置新的当前文件名
			curPath = curDir + "/" + fileListInfo.get(curPosition - 1).get("fileName".toString());
			curData = null;
			new Thread(new readData_From_File(curPath.substring(0, curPath.lastIndexOf(".")))).start();
		}

	}

	/**
	 * 显示下一个数据
	 */
	private void showNextData() {
		int curPosition = 0;
		for (int i = 0; i < fileListInfo.size(); i++) {
			//要比较必须先补全路径名
			if (curPath.equals(curDir + "/" + fileListInfo.get(i).get("fileName").toString())) {
				curPosition = i;
			}
		}
		if (curPosition == fileListInfo.size() - 1) {
			Toast.makeText(context, "已无下一个数据!", Toast.LENGTH_SHORT).show();
		} else {
			//设置新的当前文件名
			if (curDir != null) {
				curPath = curDir + "/" + fileListInfo.get(curPosition + 1).get("fileName".toString());
				curData = null;
				new Thread(new readData_From_File(curPath.substring(0, curPath.lastIndexOf(".")))).start();
			} else {
				Toast.makeText(context, "已无下一个数据!", Toast.LENGTH_SHORT).show();
			}
		}
	}


	/**
	 * 在TabActivity中无法像普通Activity中监听返回键，都是无法执行操作的，只有使用该方法才能实现要求
	 *
	 * @param event
	 * @return
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			/*弹出对话框*/
			AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("是否退出程序？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					ImportDataFromFile_Activity.this.finish();
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

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
			case R.id.showWaveBtn:
				break;
			case R.id.lastDataBtn:
				showLastData();
				break;
			case R.id.nextDataBtn:
				showNextData();
				break;
			default:
				break;
		}
	}
}
