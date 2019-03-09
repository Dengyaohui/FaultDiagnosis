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
import com.example.Activity.CurData_Activity.UpdateCurDataInChartView_TimerTask;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;

/**
 * 监听显示设置的第一项（刷新周期）
 * @author Administrator
 *
 */
public class Show_first_item_pressed implements RadialMenuItem.RadialMenuItemClickListener{

	private Context context ;
	private SharedPreferences settings;
	private RestartTimerTask_CallBack callBack;
	
	public Show_first_item_pressed(Context context,RestartTimerTask_CallBack callBack) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.settings = this.context.getSharedPreferences("settings", 0);
		this.callBack = callBack;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		final View view = LayoutInflater.from(context).inflate(R.layout.set_show_update_time_dialog, null);
		
		//从共享数据中获取上一次的设置并初始化当前哪个按钮应该被选择
		init_LastSetting(view);
		
		AlertDialog dialog = new AlertDialog.Builder(context).setTitle("显示设置").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@SuppressLint("CommitPrefEdits") @Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.set_show_interval_RadioGroup);
				int id = radioGroup.getCheckedRadioButtonId();
				//保存设置
				SharedPreferences.Editor editor = settings.edit();
				
				switch (id) {
				case R.id.oneSecond_show:
					GlobleVariable.UPDATE_CUR_DATA_PER_TIME = 1000;
					editor.putInt("UPDATE_CUR_DATA_PER_TIME", 1000);
					editor.commit();
					make_the_Set_work(callBack);
					break;
				case R.id.tenSecond_show:
					GlobleVariable.UPDATE_CUR_DATA_PER_TIME = 10000;
					editor.putInt("UPDATE_CUR_DATA_PER_TIME", 10000);
					editor.commit();
					make_the_Set_work(callBack);
					break;
				case R.id.thirtySecond_show:
					GlobleVariable.UPDATE_CUR_DATA_PER_TIME = 30000;
					editor.putInt("UPDATE_CUR_DATA_PER_TIME", 30000);
					editor.commit();
					make_the_Set_work(callBack);
					break;
				case R.id.oneMinute_show:
					GlobleVariable.UPDATE_CUR_DATA_PER_TIME = 60000;
					editor.putInt("UPDATE_CUR_DATA_PER_TIME", 60000);
					editor.commit();
					make_the_Set_work(callBack);
					break;
				case R.id.fiveMinute_show:
					GlobleVariable.UPDATE_CUR_DATA_PER_TIME = 300000;
					editor.putInt("UPDATE_CUR_DATA_PER_TIME", 300000);
					editor.commit();
					make_the_Set_work(callBack);
					break;
				case R.id.tenMinute_show:
					GlobleVariable.UPDATE_CUR_DATA_PER_TIME = 600000;
					editor.putInt("UPDATE_CUR_DATA_PER_TIME", 600000);
					editor.commit();
					make_the_Set_work(callBack);
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
		int last_UPDATE_CUR_DATA_PER_TIME = settings.getInt("UPDATE_CUR_DATA_PER_TIME", 1000);
		
		RadioButton radioButton_oneSecond = (RadioButton)view.findViewById(R.id.oneSecond_show);
		RadioButton radioButton_tenSecond = (RadioButton)view.findViewById(R.id.tenSecond_show);
		RadioButton radioButton_thirtySecond = (RadioButton)view.findViewById(R.id.thirtySecond_show);
		RadioButton radioButton_oneMinute = (RadioButton)view.findViewById(R.id.oneMinute_show );
		RadioButton radioButton_fiveMinute = (RadioButton)view.findViewById(R.id.fiveMinute_show);
		RadioButton radioButton_tenMinute = (RadioButton)view.findViewById(R.id.tenMinute_show);

		 switch (last_UPDATE_CUR_DATA_PER_TIME) {
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
	/**
	 * 使设置生效
	 */
	private void make_the_Set_work(RestartTimerTask_CallBack callBack){
		callBack.restartTimerTask();
	}
	/**
	 * 该回调 接口用于设置了更新时间后需要重新开始定时任务，设置才生效
	 * @author Administrator
	 *
	 */
	public interface RestartTimerTask_CallBack{
		public void restartTimerTask();
	}
}
