package com.example.FileUtils;

import com.example.Activity.GlobleVariable;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassReadDimensionlessdata_From_HistoryFile {

	private Date startDate = null;
	private Date endDate = null;
	public Date[] dates = null;
	
	private int type=0;
	
	public ClassReadDimensionlessdata_From_HistoryFile(Date startDate, Date endDate, int type){
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
	}
	
	/**
	 * 根据参数获得所需要的多个文件
	 * @param type
	 * @return
	 */
	private File[] getFiles(int type){
		
		File[] files = null;
		
		switch (type) {
		case GlobleVariable.SHOW_CUR_1MIN:
			files = getAllDimensionlessDataFiles_Max300();
			break;
		case GlobleVariable.SHOW_CUR_5MIN:
			files = getAllDimensionlessDataFiles_Max300();
			break;
		case GlobleVariable.SHOW_CUSTOM:
			files = getAllDimensionlessDataFiles_Custom();
			break;
		default:
			break;
		}
		return files;
	}
	
	/**
	 * 获取"每秒无量纲指标"文件夹下最多300个文件
	 * @return
	 */
	private File[] getAllDimensionlessDataFiles_Max300(){
		File curDir = null;
		curDir = new File(GlobleVariable.SD_CARD_PATH+"/"+GlobleVariable.DATA_SAVE_DIR+"/"+GlobleVariable.CUR_DIMENSION_LESS_PER_1S+"/");
		
		if(curDir.exists()){
			File[] allFiles = curDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
					return filename.endsWith(".txt");
				}
			});
			Class_SortFileAtModifiedTime sortFileList = new Class_SortFileAtModifiedTime(allFiles);
			File[] allFiles2 = sortFileList.getSortFiles();
			
			return allFiles2;
		}else {
			return null;
		}
	}
	
	/**
	 * 获取"每5分钟无量纲指标"文件夹下符号时间段区间的文件
	 * @return
	 */
	private File[] getAllDimensionlessDataFiles_Custom(){
		
		File curDir = null;
	    curDir = new File(GlobleVariable.SD_CARD_PATH+"/"+GlobleVariable.DATA_SAVE_DIR+"/"+GlobleVariable.CUR_DIMENSION_LESS_PER_5MIN+"/");
	    if(curDir.exists()){
	    	//筛选出区间范围内中的文件
			File[] allFiles = curDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					// TODO Auto-generated method stub
					if (filename.endsWith(".txt")) {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
						Date fileNameDate = null;
						filename = filename.substring(0, filename.length()-7);//2015-02-18-20-55
						try {
							fileNameDate = simpleDateFormat.parse(filename);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							System.out.println(e.toString());
						}
						if (fileNameDate.getTime() >= startDate.getTime() && fileNameDate.getTime() <= endDate.getTime()) {
							return true;
						}else {
							return false;
						}
					}else {
						return false;
					}
				}
			});
			
			if(allFiles.length == 0){
				System.out.println("没有该时间段的数据");
				return null;
			}
			//获取日期点
			dates = changeFileName2Dates(allFiles);
			//排序
			Class_SortFileAtModifiedTime sortFileList = new Class_SortFileAtModifiedTime(allFiles);
			File[] allFiles2 = sortFileList.getSortFiles();

//			System.out.println(allFiles.length);
//			for (int i = 0; i < allFiles2.length; i++) {
//			System.out.println("--->"+allFiles2[i].getName().toString());
//			}
			
			return allFiles2;
		}else {
			return null;
		}
	}
	
	/**
	 * 根据历史文件的文件名来获取日期(查看自定义时间段数据才用到)
	 * @param files
	 * @return
	 */
	public Date[] changeFileName2Dates(File[] files){
		Date[] dates = new Date[files.length];
		
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			fileName = fileName.substring(0, fileName.length()-7);//2015-02-18-20-55
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
			try {
				dates[i] = simpleDateFormat.parse(fileName);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return dates;
	}
	public Date[] getDates(){
		return dates;
	}
	
	/**
	 * 循环读取文件中各无量纲指标并整合到容器中
	 */
	public List<ArrayList<Float>> getDimensionlessData_fromFile(){
		
		List<ArrayList<Float>> All_DimensionlessData = new ArrayList<>();
		File[] allFiles = getFiles(type);
		if(allFiles!=null){
			ArrayList<Float> DimensionlessData1 = new ArrayList<>();
			ArrayList<Float> DimensionlessData2 = new ArrayList<>();
			ArrayList<Float> DimensionlessData3 = new ArrayList<>();
			ArrayList<Float> DimensionlessData4 = new ArrayList<>();
			ArrayList<Float> DimensionlessData5 = new ArrayList<>();
			ArrayList<Float> DimensionlessData6 = new ArrayList<>();
			for(File curFile : allFiles){
				File file = new File(curFile.getAbsolutePath());
				try {
					InputStream inputStream = new FileInputStream(file);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
					DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
					try {
						int i = 0;
						while(dataInputStream.available() > 0){
							DimensionlessData1.add(dataInputStream.readFloat());
							DimensionlessData2.add(dataInputStream.readFloat());
							DimensionlessData3.add(dataInputStream.readFloat());
							DimensionlessData4.add(dataInputStream.readFloat());
							DimensionlessData5.add(dataInputStream.readFloat());
							DimensionlessData6.add(dataInputStream.readFloat());
						}
					} catch (Exception e) {
						System.out.println("DataInputStream Error--->"+e.toString());
					}finally{
						dataInputStream.close();
					}
				} catch (Exception e) {
					System.out.println("Inputstream Error--->"+e.toString());
				}
			}
			
			//整合所有无量纲指标数据
			All_DimensionlessData.add(DimensionlessData1);
			All_DimensionlessData.add(DimensionlessData2);
			All_DimensionlessData.add(DimensionlessData3);
			All_DimensionlessData.add(DimensionlessData4);
			All_DimensionlessData.add(DimensionlessData5);
			All_DimensionlessData.add(DimensionlessData6);
//			/*打印测试*/
//			for (int i = 0; i < All_DimensionlessData.size(); i++) {
//				ArrayList<Float> tempList = All_DimensionlessData.get(i);
//				for(Float data:tempList){
//					System.out.println(">>>"+data);
//				}
//				System.out.println("---------------------------------------------"+"\n");
//			}
			return All_DimensionlessData;
		}
		//无返回文件列表
		else {
			return null;
		}
	}
	
}
