package com.example.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

/**
 * 原始波形显示界面
 * @author Administrator
 *
 */
public class OriginalData_Activity extends Activity{
	/**
	 * 数据显示的画布
	 */
	private SurfaceView sfv;

	public OriginalData_Activity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_data_activity);
		//得到画布和画笔
		sfv = (SurfaceView)findViewById(R.id.historySurfaceView);
		sfv.setZOrderOnTop(true);
	}

	
	
}
