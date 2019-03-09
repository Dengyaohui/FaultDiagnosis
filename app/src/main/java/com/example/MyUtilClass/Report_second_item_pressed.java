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

public class Report_second_item_pressed implements RadialMenuItem.RadialMenuItemClickListener{

	private Context context ;
	private SharedPreferences settings;
	
	public Report_second_item_pressed(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.settings = this.context.getSharedPreferences("settings", 0);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(context).inflate(R.layout.set_report_update_time_dialog, null);
		
		//从共享数据中获取上一次的设置并初始化当前哪个按钮应该被选择
		init_LastSetting(view);
		
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("诊断报告设置").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@SuppressLint("CommitPrefEdits") @Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.set_report_update_RadioGroup);
				int id = radioGroup.getCheckedRadioButtonId();
				//保存设置
				SharedPreferences.Editor editor = settings.edit();
				
				switch (id) {
				case R.id.oneSecond_update:
					GlobleVariable.UPDATE_REPORT_PER_TIME = 1000;
					editor.putInt("UPDATE_REPORT_PER_TIME", 1000);
					editor.commit();
					break;
				case R.id.tenSecond_update:
					GlobleVariable.UPDATE_REPORT_PER_TIME = 10000;
					editor.putInt("UPDATE_REPORT_PER_TIME", 10000);
					editor.commit();
					break;
				case R.id.thirtySecond_update:
					GlobleVariable.UPDATE_REPORT_PER_TIME = 30000;
					editor.putInt("UPDATE_REPORT_PER_TIME", 30000);
					editor.commit();
					break;
				case R.id.oneMinute_update:
					GlobleVariable.UPDATE_REPORT_PER_TIME = 60000;
					editor.putInt("UPDATE_REPORT_PER_TIME", 60000);
					editor.commit();
					break;
				case R.id.fiveMinute_update:
					GlobleVariable.UPDATE_REPORT_PER_TIME = 300000;
					editor.putInt("UPDATE_REPORT_PER_TIME", 300000);
					editor.commit();
					break;
				case R.id.tenMinute_update:
					GlobleVariable.UPDATE_REPORT_PER_TIME = 600000;
					editor.putInt("UPDATE_REPORT_PER_TIME", 600000);
					editor.commit();
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
		int last_UPDATE_REPORT_PER_TIME = settings.getInt("UPDATE_REPORT_PER_TIME", 1000);
		
		RadioButton radioButton_oneSecond = (RadioButton)view.findViewById(R.id.oneSecond_update);
		RadioButton radioButton_tenSecond = (RadioButton)view.findViewById(R.id.tenSecond_update);
		RadioButton radioButton_thirtySecond = (RadioButton)view.findViewById(R.id.thirtySecond_update);
		RadioButton radioButton_oneMinute = (RadioButton)view.findViewById(R.id.oneMinute_update);
		RadioButton radioButton_fiveMinute = (RadioButton)view.findViewById(R.id.fiveMinute_update);
		RadioButton radioButton_tenMinute = (RadioButton)view.findViewById(R.id.tenMinute_update);

		 switch (last_UPDATE_REPORT_PER_TIME) {
		case 1000:
			radioButton_oneSecond.setChecked(true);
			break;
		case 10000:
			radioButton_tenSecond.setChecked(true);
			break;
		case 30000:
			radioButton_thirtySecond.setChecked(true);
			break;
		case 60000:
			radioButton_oneMinute.setChecked(true);
			break;
		case 300000:
			radioButton_fiveMinute.setChecked(true);
			break;
		case 600000:
			radioButton_tenMinute.setChecked(true);
			break;
		default:
			break;
		}
	}
}
