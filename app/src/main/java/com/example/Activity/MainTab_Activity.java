package com.example.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainTab_Activity extends TabActivity implements OnClickListener,OnCheckedChangeListener{

	private Context context = this;
	/**
	 * 自定义Tab选项卡标识符
	 */
	private static final String CUR_Data="Cur_tab", DIMENSIONLESS_Data="Dimensionless_tab", ORIGINAL_DATA="Original_tab";
	/**
	 * 自定义各类Intent对象
	 */
	private Intent CurDataIntent,DimensionlessDataIntent,OriginaDatalIntent;
	/**
	 * TabHost对象
	 */
	private TabHost mTabHost;
	/**
	 * 单选按钮
	 */
	private RadioButton CurDataRadioButton,DimensionlessDataRadioButton,OriginalDataRadioButton;

	public MainTab_Activity()  {
		// TODO Auto-generated constructor stub
	}
	
	 @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置窗口特征无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

		 //将这个方法在app初始化时候调用一次，弹窗就不会出现了
		 closeAndroidPDialog();

        initUI();

		 //6.0动态权限(文件)
		 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				 != PackageManager.PERMISSION_GRANTED) {
			 showMissingPermissionDialog();//TODO：无法封装，原因暂时未知，很气
		 }
		 //6.0动态权限(录音)
		 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
				 != PackageManager.PERMISSION_GRANTED) {
			 showMissingPermissionDialog();
		 }
    }

	/**
	 * 提示用户的 dialog(6.0动态权限)
	 */
	protected void showMissingPermissionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("该应用缺少权限，可能会导致应用无法正常使用。\n\n请点击\"设置\"→\"权限管理\"打开所需权限，并重启应用。");
		// 拒绝, 退出应用
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i("info", "8--权限被拒绝,此时不会再回调onRequestPermissionsResult方法");
					}
				});
		builder.setPositiveButton(R.string.setting,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i("info", "4,需要用户手动设置，开启当前app设置界面");
						startAppSettings();
					}
				});
		builder.setCancelable(false);
		builder.show();
	}

	/**
	 * 打开     App设置界面(6.0动态权限)
	 */
	private void startAppSettings() {
		Intent intent = new Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}


	private void closeAndroidPDialog(){
		try {
			Class aClass = Class.forName("android.content.pm.PackageParser$Package");
			Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
			declaredConstructor.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Class cls = Class.forName("android.app.ActivityThread");
			Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
			declaredMethod.setAccessible(true);
			Object activityThread = declaredMethod.invoke(null);
			Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
			mHiddenApiWarningShown.setAccessible(true);
			mHiddenApiWarningShown.setBoolean(activityThread, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("deprecation")
	private void initTabData(){
		//得到Intent对象
		CurDataIntent = new Intent(context,CurData_Activity.class);
		DimensionlessDataIntent = new Intent(context,DimensionlessData_Activity.class);
		OriginaDatalIntent = new Intent(context, ImportDataFromFile_Activity.class);
    	//得到TabHost
    	mTabHost = getTabHost();
    	//给每个tab创建内容
    	mTabHost.addTab(buildTabSpec(CUR_Data,CurDataIntent));
    	mTabHost.addTab(buildTabSpec(DIMENSIONLESS_Data,DimensionlessDataIntent));
    	mTabHost.addTab(buildTabSpec(ORIGINAL_DATA,OriginaDatalIntent));
	}
	/**
     * 创建Tab的页
     * @param tag     标签名
     * @param intent  该页显示哪个Activity
     * @return
     */
    private TabHost.TabSpec buildTabSpec(String tag, Intent intent){
    	TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
    	//由于标签页不可见所有设置的指示器也不起作用
    	tabSpec.setContent(intent).setIndicator("");
    	return tabSpec;
    }

	private void initUI(){
		//初始化tab中数据
		initTabData();
		//得到单选按钮
		CurDataRadioButton = findViewById(R.id.radio_CurData);
		DimensionlessDataRadioButton = findViewById(R.id.radio_dimensionlessData);
		OriginalDataRadioButton = findViewById(R.id.radio_originalData);

		CurDataRadioButton.setOnCheckedChangeListener(this);
		DimensionlessDataRadioButton.setOnCheckedChangeListener(this);
		OriginalDataRadioButton.setOnCheckedChangeListener(this);
		
		//设置当前默认Tab
    	CurDataRadioButton.setChecked(true);
    	mTabHost.setCurrentTabByTag(CUR_Data);
	}


	/**
	* 点击切换页面
	* */
	@Override
	public void onCheckedChanged(CompoundButton radionButton, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			switch (radionButton.getId()) {
			case R.id.radio_CurData:
				mTabHost.setCurrentTabByTag(CUR_Data);
				break;
			case R.id.radio_dimensionlessData:
				mTabHost.setCurrentTabByTag(DIMENSIONLESS_Data);
//				getRunningActivityName();
//				Log.e("=====",getRunningActivityName());
				break;
			case R.id.radio_originalData:
				mTabHost.setCurrentTabByTag(ORIGINAL_DATA);
				break;
			default:
				break;
			}
		}
	}

	//用于debug时获取当前运行的Activity
	private String getRunningActivityName(){
		ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		return runningActivity;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
