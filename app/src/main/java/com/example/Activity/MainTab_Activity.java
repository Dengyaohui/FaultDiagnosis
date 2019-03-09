package com.example.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        initUI();
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
		CurDataRadioButton = (RadioButton)findViewById(R.id.radio_CurData);
		DimensionlessDataRadioButton = (RadioButton)findViewById(R.id.radio_dimensionlessData);
		OriginalDataRadioButton = (RadioButton)findViewById(R.id.radio_originalData);
		CurDataRadioButton.setOnCheckedChangeListener(this);
		DimensionlessDataRadioButton.setOnCheckedChangeListener(this);
		OriginalDataRadioButton.setOnCheckedChangeListener(this);
		
		//设置当前默认Tab
    	CurDataRadioButton.setChecked(true);
    	mTabHost.setCurrentTabByTag(CUR_Data);
	}
	

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
				break;
			case R.id.radio_originalData:
				mTabHost.setCurrentTabByTag(ORIGINAL_DATA);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
