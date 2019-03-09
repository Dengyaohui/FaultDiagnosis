package com.example.MyUtilClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.Activity.GlobleVariable;
import com.example.Activity.R;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;

/**
 * 监听诊断报告设置的第一项(诊断间隔)
 * @author Administrator
 *
 */
public class Report_first_item_pressed implements RadialMenuItem.RadialMenuItemClickListener{

	private Context context ;
	private SharedPreferences settings;
	public Report_first_item_pressed(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.settings = this.context.getSharedPreferences("settings", 0);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		final View view = LayoutInflater.from(context).inflate(R.layout.set_report_interval_dialog, null);
		
		//从共享数据中获取上一次的设置并初始化当前哪个按钮应该被选择
		init_LastSetting(view);
		
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("诊断报告设置").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@SuppressLint("CommitPrefEdits") @Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.set_report_interval_RadioGroup);
				int id = radioGroup.getCheckedRadioButtonId();
				//保存设置
				SharedPreferences.Editor editor = settings.edit();
				
				switch (id) {
				case R.id.oneSecond:
					GlobleVariable.CREATE_REPORT_PER_COUNT = 1;
					editor.putInt("CREATE_REPORT_PER_COUNT", 1);
					editor.commit();
					GlobleVariable.CREATE_REPORT_COUNT=0;      //不加这句当改变了诊断间隔后将永远也不会再更新
					break;
				case R.id.tenSecond:
					GlobleVariable.CREATE_REPORT_PER_COUNT = 10;
					editor.putInt("CREATE_REPORT_PER_COUNT", 10);
					editor.commit();
					GlobleVariable.CREATE_REPORT_COUNT=0;
					break;
				case R.id.thirtySecond:
					GlobleVariable.CREATE_REPORT_PER_COUNT = 30;
					editor.putInt("CREATE_REPORT_PER_COUNT", 30);
					editor.commit();
					GlobleVariable.CREATE_REPORT_COUNT=0;
					break;
				case R.id.oneMinute:
					GlobleVariable.CREATE_REPORT_PER_COUNT = 60;
					editor.putInt("CREATE_REPORT_PER_COUNT", 60);
					editor.commit();
					GlobleVariable.CREATE_REPORT_COUNT=0;
					break;
				case R.id.fiveMinute:
					GlobleVariable.CREATE_REPORT_PER_COUNT = 300;
					editor.putInt("CREATE_REPORT_PER_COUNT", 300);
					editor.commit();
					GlobleVariable.CREATE_REPORT_COUNT=0;
					break;
				case R.id.tenMinute:
					GlobleVariable.CREATE_REPORT_PER_COUNT = 600;
					editor.putInt("CREATE_REPORT_PER_COUNT", 600);
					editor.commit();
					GlobleVariable.CREATE_REPORT_COUNT=0;
					break;
				default:
					break;
				}
			}
		}).create();
		dialog.show();
	}
	/**
	 * 初始化上次设置状态
	 */
	private void init_LastSetting(View view){
		int last_CREATE_REPORT_PER_COUNT = settings.getInt("CREATE_REPORT_PER_COUNT", 1);
		
		RadioButton radioButton_oneSecond = (RadioButton)view.findViewById(R.id.oneSecond);
		RadioButton radioButton_tenSecond = (RadioButton)view.findViewById(R.id.tenSecond);
		RadioButton radioButton_thirtySecond = (RadioButton)view.findViewById(R.id.thirtySecond);
		RadioButton radioButton_oneMinute = (RadioButton)view.findViewById(R.id.oneMinute );
		RadioButton radioButton_fiveMinute = (RadioButton)view.findViewById(R.id.fiveMinute);
		RadioButton radioButton_tenMinute = (RadioButton)view.findViewById(R.id.tenMinute);

		 switch (last_CREATE_REPORT_PER_COUNT) {
		case 1:
			radioButton_oneSecond.setChecked(true);
			break;
		case 10:
			radioButton_tenSecond.setChecked(true);
			break;
		case 30:
			radioButton_thirtySecond.setChecked(true);
			break;
		case 60:
			radioButton_oneMinute.setChecked(true);
			break;
		case 300:
			radioButton_fiveMinute.setChecked(true);
			break;
		case 600:
			radioButton_tenMinute.setChecked(true);
			break;
		default:
			break;
		}
	}
	
}
