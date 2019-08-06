package com.example.MyUtilClass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static com.example.MyThread.ClassReadPCMdata_From_Web.volet;

/**
 *
 */

public class Util extends FragmentActivity {
	private static Toast toast;

	//所有的Toast必须使用该方法！
	//修复了多次点击导致Toast显示时间过长的BUG，用户体验感极好
	//因为toast的实现需要在activity的主线程才能正常工作，所以传统的非主线程不能使toast显示在actvity上，通过Handler可以使自定义线程运行于Ui主线程。
	public static void showToast(Context context, String content) {

		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}

		toast.show();

	}

	public void ClickLeft(View view) {
		finish();
	}

	/**
	 * 提示用户设置权限的 dialog
	 */
	public void showMissingPermissionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("当前应用缺少对应权限。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");
		// 拒绝, 退出应用
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i("info", "8--权限被拒绝,此时不会再回调onRequestPermissionsResult方法");
					}
				});
		builder.setPositiveButton("设置",
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
	 * 打开     App设置界面
	 */
	private void startAppSettings() {
		Intent intent = new Intent(
				Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}

	public static void getData(float[] DimensionlessDataFromWeb,String[] DataFromWeb){
		for(int i =0; i < DimensionlessDataFromWeb.length - 1; i ++){
			try {
				DimensionlessDataFromWeb[i] = Float.parseFloat(DataFromWeb[i]);
			} catch (Exception e) {
				DimensionlessDataFromWeb[i] = 0.000f;
				System.out.println("接收到不规范数据------->" + e.toString());
			}
		}
		DimensionlessDataFromWeb[5] =volet[0];
	}

}
