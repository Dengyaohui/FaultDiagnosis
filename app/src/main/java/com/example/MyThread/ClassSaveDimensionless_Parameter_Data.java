package com.example.MyThread;

import android.os.Environment;

import com.example.Activity.GlobleVariable;
import com.example.FileUtils.ClassDealPCMdata;
import com.example.FileUtils.Class_FileTool;
import com.example.FileUtils.Class_SortFileAtModifiedTime;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 计算并保存无量纲指标线程
 * @author Administrator
 *
 */
public class ClassSaveDimensionless_Parameter_Data extends Thread{

	/**
	 * 从读数据类中传过来的一秒所采集的数据
	 */
	private short[] tmpBuf;
	/**
	 * 保存到的文件夹名
	 */
	private String saveDirName;
	/**
	 * 保存的文件名
	 */
	private String saveFileName;
	/**
	 * 控制保存最多多少文件
	 */
	private int fileSaveMax;
	/**
	 * 保存计算后的无量纲指标值
	 */
	private float[] DimensionlessData;
	/**
	 * 文件操作工具类
	 */
	private Class_FileTool fileTool;
	
	/**
	 * @param tmpBuf        保存的数据
	 * @param saveDirName   保存到哪个文件夹
	 * @param saveFileName  以什么文件名保存
	 * @param fileSaveMax   保存的最大数量
	 */
	public ClassSaveDimensionless_Parameter_Data(short[] tmpBuf, String saveDirName, String saveFileName, int fileSaveMax){
		this.tmpBuf = tmpBuf;
		this.saveDirName = saveDirName;
		this.saveFileName = saveFileName;
		this.fileSaveMax = fileSaveMax;
		DimensionlessData = new float[6];
		fileTool = new Class_FileTool();
	}
	
	/**
	 * 根据最大保存数量来清理旧的文件
	 * @return
	 */
	public int cleanSomeOldFile(String curDirName){
		//列出当前所有文件
		File curDir = new File(GlobleVariable.SD_CARD_PATH+"/"+GlobleVariable.DATA_SAVE_DIR+"/"+curDirName+"/");
		//文件顺序不能保证
		File[] curfiles = curDir.listFiles();

		//按最后修改日期排序（最新的数据在前面）
		Class_SortFileAtModifiedTime sortFiles = new Class_SortFileAtModifiedTime(curfiles);
		File[] curfiles2 = sortFiles.getSortFiles();
		//删除最旧的文件
		if(curfiles2.length >=  GlobleVariable.PER_1S_DIMENSION_LESS_FILE_MAX_NUM){
			for (int i = curfiles2.length; i > GlobleVariable.PER_1S_DIMENSION_LESS_FILE_MAX_NUM; i--) {
				File deleteFile = new File(curfiles2[i-1].getAbsolutePath());
				deleteFile.delete();
			}
		}else {
			System.out.println("未到5分钟文件上限，不用删除");
		}
		
		
		return 0;
	}
	
	/**
	 * 保存无量纲数据
	 * @param dirName
	 * @param fileName
	 * @param Dimensionlessdata   无量纲指标值数组（烈度，峰值....）
	 * @throws IOException
	 */
	private void SaveDimensionlessdata(String dirName, String fileName, float[] Dimensionlessdata) throws IOException{
		//先判断是否有内存卡
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			File Dimensionfile=null;
			
			fileTool.createDir_onSD(dirName);
			try {
				Dimensionfile = fileTool.createFile_onSD(dirName, fileName);
			} catch (IOException e) {
				System.out.println("Create file Error ------>"+e.toString());
			}
			
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(Dimensionfile);
			} catch (FileNotFoundException e) {
				System.out.println("new FileOutputStream(Dimensionfile) error----->"+e.toString());;
			}
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
			
			try {
				for (int i = 0; i < Dimensionlessdata.length; i++) {
					dataOutputStream.writeFloat(Dimensionlessdata[i]);
				}
			} catch (Exception e) {
				System.out.println("写入无量纲指标失败------->"+e.toString());
			}finally{
				dataOutputStream.close();
			}
			
			//判断最大保存数量来清理当前文件夹下的文件（保存最新的n条记录）
			cleanSomeOldFile(dirName);
		}else {
			//提示没有内存卡
		}
	}
	
	@Override
	public void run() {
		try {
				for (int i = 0; i < 6; i++) {
					ClassDealPCMdata dealPCMdata = new ClassDealPCMdata(tmpBuf, i);
					DimensionlessData[i] = dealPCMdata.getDimensionless();
//					System.out.println("DimensionlessData: >>>>>>>>>>>"+DimensionlessData[i]);
				}
				//这里保存的一条数据包含无量纲指标6种值
				SaveDimensionlessdata(saveDirName, saveFileName, DimensionlessData);
		} catch (IOException e) {
			System.out.println("保存无量纲指标失败 ------->"+e.toString());
		}

	}
}
