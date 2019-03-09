package com.example.MyUtilClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.example.Activity.GlobleVariable;
import com.example.Activity.R;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;

public class Show_second_item_pressed  implements RadialMenuItem.RadialMenuItemClickListener{

	private Context context ;
	private SharedPreferences settings;
	
	public Show_second_item_pressed(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(context).inflate(R.layout.set_show_update_time_dialog, null);
	
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("显示设置").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@SuppressLint("CommitPrefEdits") @Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		}).create();
		dialog.show();
	}

}
