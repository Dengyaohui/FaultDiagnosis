package com.example.MyThread;

import android.os.Environment;

import com.example.Activity.GlobleVariable;
import com.example.FileUtils.Class_FileTool;
import com.example.FileUtils.Class_SortFileAtModifiedTime;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 保存PCM原始数据线程(每秒保存一次)
 * @author Administrator
 *
 */
public class ClassSavePCMdata extends Thread{

	/**
	 * 从读数据类中传过来的一秒数据
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
	 * 文件操作工具类
	 */
	private Class_FileTool fileTool;
	
	
	/**
	 *  @param tmpBuf        保存的数据
	 * @param saveDirName   保存到哪个文件夹
	 * @param saveFileName  以什么文件名保存
	 * @param fileSaveMax   保存的最大数量
	 */
	public ClassSavePCMdata(short[] tmpBuf, String saveDirName, String saveFileName, int fileSaveMax) {
		this.tmpBuf = tmpBuf;
		this.saveDirName = saveDirName;
		this.saveFileName = saveFileName;
		this.fileSaveMax = fileSaveMax;
		fileTool = new Class_FileTool();
	}
	
	/**
	 * 根据最大保存数量来清理旧的文件
	 * @return
	 */
	public int cleanSomeOldFile(String curDirName){
		//列出当前所有的.pcm文件
		File curDir = new File(GlobleVariable.SD_CARD_PATH+"/"+GlobleVariable.DATA_SAVE_DIR+"/"+curDirName+"/");
		//文件顺序不能保证
		File[] curfiles = curDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String fileName) {
				// TODO Auto-generated method stub
				return fileName.endsWith(".pcm");
			}
		});

		//按最后修改日期排序
		Class_SortFileAtModifiedTime sortFiles = new Class_SortFileAtModifiedTime(curfiles);
		File[] curfiles2 = sortFiles.getSortFiles();
		//删除最旧的文件

		if(curfiles2.length >=  GlobleVariable.PER_1S_HISTORY_FILE_MAX_NUM){
			if(curfiles2.length >=  GlobleVariable.PER_1S_HISTORY_FILE_MAX_NUM){
				for (int i = curfiles2.length; i > GlobleVariable.PER_1S_HISTORY_FILE_MAX_NUM; i--) {
					File deleteFile = new File(curfiles2[i-1].getAbsolutePath());
					System.out.println("Clean : "+curfiles2[i-1].getName());
					deleteFile.delete();
				}
			}
		}else {
			System.out.println("do not clean");
		}
		
		
		return 0;
	}
	
	/**
	 * 保存原始PCM数据
	 * @param dirName
	 * @param fileName
	 * @throws IOException
	 */
	private void SavePCMdata(String dirName, String fileName) throws IOException{
		System.out.println("Save PCM Data at Time: ");
		//先判断是否有内存卡
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			File PCMfile=null;
			
			fileTool.createDir_onSD(dirName);
			try {
				PCMfile = fileTool.createFile_onSD(dirName, fileName);
			} catch (IOException e) {
				System.out.println("Create file Error ------>"+e.toString());
			}
			
			OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(PCMfile);
			} catch (FileNotFoundException e) {
				System.out.println("new FileOutputStream(PCMfile) error----->"+e.toString());
			}
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);
			
			try {
				for (int i = 0; i < tmpBuf.length; i++) {
					dataOutputStream.writeShort(tmpBuf[i]);
				}
			} catch (Exception e) {
				System.out.println("write pcm data to file error------->"+e.toString());
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
			 SavePCMdata(saveDirName, saveFileName);
		} catch (IOException e) {
			System.out.println("SavePCMdata(saveDirName, saveFileName) error ------->"+e.toString());
		}

	}
	
}
