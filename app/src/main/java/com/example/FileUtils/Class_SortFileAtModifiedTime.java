package com.example.FileUtils;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 对文件列表按修改时间降序排序（最新的数据在最前面）
 * @author Administrator
 *
 */
public class Class_SortFileAtModifiedTime {

	private File[] files;
	
	public Class_SortFileAtModifiedTime(File[] files){
		this.files = files;
	}
	
	public File[] getSortFiles(){
		File[] returnFiles;
		
		Map<Date, File> treeMap = new TreeMap<>(new MyComparator());
		for (int i = 0; i < files.length; i++) {
			treeMap.put(new Date(files[i].lastModified()), files[i]);
		}
		returnFiles = treeMap.values().toArray(new File[0]);
//		Log.e("returnFiles", Arrays.toString(returnFiles));

		return returnFiles;
	}
	
	class MyComparator implements Comparator<Date>{
		//降序
		@Override
		public int compare(Date date1, Date date2) {
			return date2.compareTo(date1);
		}
		
	}
}
