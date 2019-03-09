package com.example.MyUtilClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.Activity.GlobleVariable;
import com.example.Activity.R;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;

public class Report_third_item_pressed implements RadialMenuItem.RadialMenuItemClickListener{

	private Context context ;
	private SharedPreferences settings;
	
	public Report_third_item_pressed(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.settings = this.context.getSharedPreferences("settings", 0);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(context).inflate(R.layout.set_report_warning_line_dialog, null);
		
		//从共享数据中获取上一次的设置并初始化当前哪个按钮应该被选择
    	init_LastSetting(view);
		
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("诊断报告设置").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@SuppressLint("CommitPrefEdits") @Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				EditText liedu_high_line = (EditText)view.findViewById(R.id.liedu_high_line);
				EditText liedu_low_line = (EditText)view.findViewById(R.id.liedu_low_line);

				EditText maichong_high_line = (EditText)view.findViewById(R.id.maichong_high_line);
				EditText maichong_low_line = (EditText)view.findViewById(R.id.maichong_low_line);

				EditText yudu_high_line = (EditText)view.findViewById(R.id.yudu_high_line);
				EditText yudu_low_line = (EditText)view.findViewById(R.id.yudu_low_line);
				
				EditText fengzhi_high_line = (EditText)view.findViewById(R.id.fengzhi_high_line);
				EditText fengzhi_low_line = (EditText)view.findViewById(R.id.fengzhi_low_line);
				
				EditText qiaodu_high_line = (EditText)view.findViewById(R.id.qiaodu_high_line);
				EditText qiaodu_low_line = (EditText)view.findViewById(R.id.qiaodu_low_line);
				
				EditText boxing_high_line = (EditText)view.findViewById(R.id.boxing_high_line);
				EditText boxing_low_line = (EditText)view.findViewById(R.id.boxing_low_line);
				
				//保存设置
				SharedPreferences.Editor editor = settings.edit();
				
				if (!liedu_high_line.getText().toString().equals("") && !liedu_low_line.getText().toString().equals("")) {
					float liedu_high = Float.parseFloat(liedu_high_line.getText().toString());
					float liedu_low = Float.parseFloat(liedu_low_line.getText().toString());
					if (liedu_high>100 || liedu_low<-100) {
						//超过一定范围
								
					}else {
						if (liedu_high > liedu_low) {
							GlobleVariable.LIEDU_HIGH_WARING = liedu_high;
							GlobleVariable.LIEDU_LOW_WARING = liedu_low;
							editor.putFloat("LIEDU_HIGH_WARING", liedu_high);
							editor.putFloat("LIEDU_LOW_WARING", liedu_low);
							editor.commit();
						}
					}
				}
				
				if (!maichong_high_line.getText().toString().equals("") && !maichong_low_line.getText().toString().equals("")) {
					float maichong_high = Float.parseFloat(maichong_high_line.getText().toString());
					float maichong_low = Float.parseFloat(maichong_low_line.getText().toString());
					if (maichong_high>100 || maichong_low<-100) {
						//超过一定范围
								
					}else {
						if (maichong_high > maichong_low) {
							GlobleVariable.MAICHONG_HIGH_WARING = maichong_high;
							GlobleVariable.MAICHONG_LOW_WARING = maichong_low;
							editor.putFloat("MAICHONG_HIGH_WARING", maichong_high);
							editor.putFloat("MAICHONG_LOW_WARING", maichong_low);
							editor.commit();
						}
						
					}
				}
				
				if (!yudu_high_line.getText().toString().equals("") && !yudu_low_line.getText().toString().equals("")) {
					float yudu_high = Float.parseFloat(yudu_high_line.getText().toString());
					float yudu_low = Float.parseFloat(yudu_low_line.getText().toString());
					if (yudu_high>100 || yudu_low<-100) {
						//超过一定范围
								
					}else {
						if (yudu_high > yudu_low) {
							GlobleVariable.YUDU_HIGH_WARING = yudu_high;
							GlobleVariable.YUDU_LOW_WARING = yudu_low;
							editor.putFloat("YUDU_HIGH_WARING", yudu_high);
							editor.putFloat("YUDU_LOW_WARING", yudu_low);
							editor.commit();
						}
					}
				}
				
				if (!fengzhi_high_line.getText().toString().equals("") && !fengzhi_high_line.getText().toString().equals("")) {
					float fengzhi_high = Float.parseFloat(fengzhi_high_line.getText().toString());
					float fengzhi_low = Float.parseFloat(fengzhi_low_line.getText().toString());
					if (fengzhi_high>100 || fengzhi_low<-100) {
						//超过一定范围
								
					}else {
						if (fengzhi_high > fengzhi_low) {
							GlobleVariable.FENGZHI_HIGH_WARING = fengzhi_high;
							GlobleVariable.FENGZHI_LOW_WARING = fengzhi_low;
							editor.putFloat("FENGZHI_HIGH_WARING", fengzhi_high);
							editor.putFloat("FENGZHI_LOW_WARING", fengzhi_low);	
							editor.commit();
						}
					}
				}
				
				if (!qiaodu_high_line.getText().toString().equals("") && !qiaodu_high_line.getText().toString().equals("")) {
					float qiaodu_high = Float.parseFloat(qiaodu_high_line.getText().toString());
					float qiaodu_low = Float.parseFloat(qiaodu_low_line.getText().toString());
					if (qiaodu_high>100 || qiaodu_low<-100) {
						//超过一定范围
								
					}else {
						if (qiaodu_high > qiaodu_low) {
							GlobleVariable.QIAODU_HIGH_WARING = qiaodu_high;
							GlobleVariable.QIAODU_LOW_WARING = qiaodu_low;
							editor.putFloat("QIAODU_HIGH_WARING", qiaodu_high);
							editor.putFloat("QIAODU_LOW_WARING", qiaodu_low);
							editor.commit();
						}
					}
				}
				
				if (!boxing_high_line.getText().toString().equals("") && !boxing_high_line.getText().toString().equals("")) {
					float boxing_high = Float.parseFloat(boxing_high_line.getText().toString());
					float boxing_low = Float.parseFloat(boxing_low_line.getText().toString());
					if (boxing_high>100 || boxing_low<-100) {
						//超过一定范围
								
					}else {
						if (boxing_high > boxing_low) {
							GlobleVariable.BOXING_HIGH_WARING = boxing_high;
							GlobleVariable.BOXING_LOW_WARING = boxing_low;
							editor.putFloat("BOXING_HIGH_WARING", boxing_high);
							editor.putFloat("BOXING_LOW_WARING", boxing_low);
							editor.commit();
						}
					}
				}
			}
		}).create();
		dialog.show();
	}
	
	/**
	 * 初始化上次设置状态
	 */
	private void init_LastSetting(View view){
		
		EditText liedu_high_line = (EditText)view.findViewById(R.id.liedu_high_line);
		EditText liedu_low_line = (EditText)view.findViewById(R.id.liedu_low_line);

		EditText maichong_high_line = (EditText)view.findViewById(R.id.maichong_high_line);
		EditText maichong_low_line = (EditText)view.findViewById(R.id.maichong_low_line);

		EditText yudu_high_line = (EditText)view.findViewById(R.id.yudu_high_line);
		EditText yudu_low_line = (EditText)view.findViewById(R.id.yudu_low_line);
		
		EditText fengzhi_high_line = (EditText)view.findViewById(R.id.fengzhi_high_line);
		EditText fengzhi_low_line = (EditText)view.findViewById(R.id.fengzhi_low_line);
		
		EditText qiaodu_high_line = (EditText)view.findViewById(R.id.qiaodu_high_line);
		EditText qiaodu_low_line = (EditText)view.findViewById(R.id.qiaodu_low_line);
		
		EditText boxing_high_line = (EditText)view.findViewById(R.id.boxing_high_line);
		EditText boxing_low_line = (EditText)view.findViewById(R.id.boxing_low_line);

		liedu_high_line.setText(String.valueOf(settings.getFloat("LIEDU_HIGH_WARING", (float) 5.000)));
		liedu_low_line.setText(String.valueOf(settings.getFloat("LIEDU_LOW_WARING", -(float) 5.000)));

		maichong_high_line.setText(String.valueOf(settings.getFloat("MAICHONG_HIGH_WARING", (float) 5.000)));
		maichong_low_line.setText(String.valueOf(settings.getFloat("MAICHONG_LOW_WARING", -(float) 5.000)));
	
		yudu_high_line.setText(String.valueOf(settings.getFloat("YUDU_HIGH_WARING", (float) 5.000)));
		yudu_low_line.setText(String.valueOf(settings.getFloat("YUDU_LOW_WARING", -(float) 5.000)));

		fengzhi_high_line.setText(String.valueOf(settings.getFloat("FENGZHI_HIGH_WARING", (float) 5.000)));
		fengzhi_low_line.setText(String.valueOf(settings.getFloat("FENGZHI_LOW_WARING", -(float) 5.000)));

		qiaodu_high_line.setText(String.valueOf(settings.getFloat("QIAODU_HIGH_WARING", (float) 5.000)));
		qiaodu_low_line.setText(String.valueOf(settings.getFloat("QIAODU_LOW_WARING", -(float) 5.000)));

		boxing_high_line.setText(String.valueOf(settings.getFloat("BOXING_HIGH_WARING", (float) 5.000)));
		boxing_low_line.setText(String.valueOf(settings.getFloat("BOXING_LOW_WARING", -(float) 5.000)));

	}

}
