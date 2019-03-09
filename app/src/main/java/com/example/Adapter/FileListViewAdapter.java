package com.example.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.Activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListViewAdapter extends BaseAdapter{

	private Context context;
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	
	public FileListViewAdapter(Context context){
		super();
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		FileItemHolder holder;
		if(null == convertView){
			holder = new FileItemHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_show_history_filelist_item, null);
			holder.fileIcon = (ImageView)convertView.findViewById(R.id.file_Item_Icon);
			holder.fileName = (TextView)convertView.findViewById(R.id.file_Item_Name);
			convertView.setTag(holder);
		}else {
			holder = (FileItemHolder)convertView.getTag();
		}
		
		holder.fileIcon.setImageResource((Integer)(list.get(position).get("icon")));
		holder.fileName.setText((String)(list.get(position).get("name")));
		
		return convertView;
	}
	
	public class FileItemHolder{
		public ImageView fileIcon;
		public TextView fileName;
	}
	
	//传进信息列表
	public void setFileListInfo(List<Map<String, Object>> infos){
		list.clear();
		list.addAll(infos);
		notifyDataSetChanged();
	} 

}
