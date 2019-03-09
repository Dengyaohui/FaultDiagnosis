package com.example.Adapter;

import java.util.List;

import com.example.Activity.R;
import com.example.MyPacketClass.DimensionlessReport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyReportListViewAdapter extends BaseAdapter{

	private Context context;             //上下文对象引用  
	private List<DimensionlessReport> reports = null;
	private DimensionlessReport report;
	
	public MyReportListViewAdapter(Context context,List<DimensionlessReport> reports) {
		this.context=context;
		this.reports = reports;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.reports.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return reports.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		// TODO Auto-generated method stub
		reportViewHolder holder = null;
		if (converView==null) {
			holder=new reportViewHolder();
			converView = LayoutInflater.from(context).inflate(R.layout.report_menu_list, null);
			
			holder.timeLabel = (TextView)converView.findViewById(R.id.time_label_textview);
			
			holder.liedu_data = (TextView)converView.findViewById(R.id.liedu_data_textview);
			holder.maichong_data = (TextView)converView.findViewById(R.id.maichong_data_textview);
			holder.yudu_data = (TextView)converView.findViewById(R.id.yudu_data_textview);
			holder.fengzhi_data = (TextView)converView.findViewById(R.id.fengzhi_data_textview);
			holder.qiaodu_data = (TextView)converView.findViewById(R.id.qiaodu_data_textview);
			holder.boxing_data = (TextView)converView.findViewById(R.id.boxing_data_textview);
		
			holder.liedu_state = (TextView)converView.findViewById(R.id.liedu_state_textview);
			holder.maichong_state = (TextView)converView.findViewById(R.id.maichong_state_textview);
			holder.yudu_state = (TextView)converView.findViewById(R.id.yudu_state_textview);
			holder.fengzhi_state = (TextView)converView.findViewById(R.id.fengzhi_state_textview);
			holder.qiaodu_state = (TextView)converView.findViewById(R.id.qiaodu_state_textview);
			holder.boxing_state = (TextView)converView.findViewById(R.id.boxing_state_textview);
			
			holder.liedu_state_logo = (ImageView)converView.findViewById(R.id.liedu_state_logo);
			holder.maichong_state_logo = (ImageView)converView.findViewById(R.id.maichong_state_logo);
			holder.yudu_state_logo = (ImageView)converView.findViewById(R.id.yudu_state_logo);
			holder.fengzhi_state_logo = (ImageView)converView.findViewById(R.id.fengzhi_state_logo);
			holder.qiaodu_state_logo = (ImageView)converView.findViewById(R.id.qiaodu_state_logo);
			holder.boxing_state_logo = (ImageView)converView.findViewById(R.id.boxing_state_logo);
		
			converView.setTag(holder);
		}else {
			holder = (reportViewHolder)converView.getTag();
		}
		
		//设置值
		report = reports.get(position);
		
		holder.timeLabel.setText(report.getDateTime());
		
		holder.liedu_data.setText(String.valueOf(report.getLiedu_data()));
		holder.maichong_data.setText(String.valueOf(report.getMaichong_data()));
		holder.yudu_data.setText(String.valueOf(report.getYudu_data()));
		holder.fengzhi_data.setText(String.valueOf(report.getFengzhi_data()));
		holder.qiaodu_data.setText(String.valueOf(report.getQiaodu_data()));
		holder.boxing_data.setText(String.valueOf(report.getBoxing_data()));
		
		holder.liedu_state.setText(report.getLiedu_state());
		holder.maichong_state.setText(report.getMaichong_state());
		holder.yudu_state.setText(report.getYudu_state());
		holder.fengzhi_state.setText(report.getFengzhi_state());
		holder.qiaodu_state.setText(report.getQiaodu_state());
		holder.boxing_state.setText(report.getBoxing_state());
		
		if (report.getLiedu_state().equals("正常")) {
			holder.liedu_state_logo.setImageResource(R.drawable.state_normal_logo);
		}else {
			holder.liedu_state_logo.setImageResource(R.drawable.state_warning_logo);
		}
		if (report.getMaichong_state().equals("正常")) {
			holder.maichong_state_logo.setImageResource(R.drawable.state_normal_logo);
		}else {
			holder.maichong_state_logo.setImageResource(R.drawable.state_warning_logo);
		}
		if (report.getYudu_state().equals("正常")) {
			holder.yudu_state_logo.setImageResource(R.drawable.state_normal_logo);
		}else {
			holder.yudu_state_logo.setImageResource(R.drawable.state_warning_logo);
		}
		if (report.getFengzhi_state().equals("正常")) {
			holder.fengzhi_state_logo.setImageResource(R.drawable.state_normal_logo);
		}else {
			holder.fengzhi_state_logo.setImageResource(R.drawable.state_warning_logo);
		}
		if (report.getQiaodu_state().equals("正常")) {
			holder.qiaodu_state_logo.setImageResource(R.drawable.state_normal_logo);
		}else {
			holder.qiaodu_state_logo.setImageResource(R.drawable.state_warning_logo);
		}
		if (report.getBoxing_state().equals("正常")) {
			holder.boxing_state_logo.setImageResource(R.drawable.state_normal_logo);
		}else {
			holder.boxing_state_logo.setImageResource(R.drawable.state_warning_logo);
		}
		
		return converView;
	}

	
	/**
	 * 定义一个内部类 
     * 声明相应的控件引用 
	 * @author Administrator
	 */
	public  class reportViewHolder{
		//所有控件对象引用  
		public TextView timeLabel;    
		
        public TextView liedu_data; 
        public TextView maichong_data;    
        public TextView yudu_data;    
        public TextView fengzhi_data;    
        public TextView qiaodu_data;    
        public TextView boxing_data;    
          
        public TextView liedu_state; 
        public TextView maichong_state;    
        public TextView yudu_state;    
        public TextView fengzhi_state;    
        public TextView qiaodu_state;    
        public TextView boxing_state; 
        
        public ImageView liedu_state_logo; 
        public ImageView maichong_state_logo;    
        public ImageView yudu_state_logo;    
        public ImageView fengzhi_state_logo;    
        public ImageView qiaodu_state_logo;    
        public ImageView boxing_state_logo;
	}
}
