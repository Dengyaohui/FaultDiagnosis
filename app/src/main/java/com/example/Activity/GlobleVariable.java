package com.example.Activity;

import android.os.Environment;

public class GlobleVariable {

/**
 *说明接收数据的方式，false为现场，true为云端
 */
	public static boolean ReadFromWeb;

	/**
	 * SD卡路径名
	 */
	public final static String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	/**
	 * 故障诊断数据保存的文件夹
	 */
	public final static String DATA_SAVE_DIR = "故障诊断数据";
//	public final static String WEB_DATA_SAVE_DIR = "故障诊断云端数据";
//	public final static String DIMENSION_LES_SAVE_DIR = "无量纲诊断数据";
	/**
	 * 当前每秒原始数据文件夹
	 */
	public final static String CUR_ORIGINAL_DATA_PER_1S = "每秒原始数据";
	public final static String WEB_CUR_ORIGINAL_DATA_PER_1S = "云端每秒原始数据";
	public final static String CUR_ORIGINAL_DATA_PER_5MIN = "每5分钟原始数据";
	public final static String WEB_CUR_ORIGINAL_DATA_PER_5MIN = "云端每5分钟原始数据";
	/**
	 * 当前每秒无量纲指标文件夹
	 */
	public final static String CUR_DIMENSION_LESS_PER_1S      = "每秒无量纲指标";
	public final static String WEB_CUR_DIMENSION_LESS_PER_1S      = "云端每秒无量纲指标";
	/**
	 * 当前每5分钟无量纲指标文件夹
	 */
	public final static String CUR_DIMENSION_LESS_PER_5MIN    = "每5分钟无量纲指标";	
	
	
	/**
	 * 当前无量纲指标--->烈度
	 */
	public final static String CUR_DIMENSION1 = "烈度指标";
	/**
	 * 当前无量纲指标--->脉冲
	 */
	public final static String CUR_DIMENSION2 = "脉冲指标";
	/**
	 * 当前无量纲指标--->欲度
	 */
	public final static String CUR_DIMENSION3 = "欲度指标";
	/**
	 * 当前无量纲指标--->峰值
	 */
	public final static String CUR_DIMENSION4 = "峰值指标";
	/**
	 * 当前无量纲指标--->峭度
	 */
	public final static String CUR_DIMENSION5 = "峭度指标";
	/**
	 * 当前无量纲指标--->波形
	 */
	public final static String CUR_DIMENSION6 = "波形指标";

	/*************************************************警告线设置参数*************************************************************/	
	/**
	 * 	无量纲指标过高警告线
	 */
	public static float LIEDU_HIGH_WARING = (float) 5.000;
	public static float MAICHONG_HIGH_WARING = (float) 5.000;
	public static float YUDU_HIGH_WARING = (float) 5.000;
	public static float FENGZHI_HIGH_WARING = (float) 5.000;
	public static float QIAODU_HIGH_WARING = (float) 5.000;
	public static float BOXING_HIGH_WARING = (float) 5.000;
	/**
	 * 	无量纲指标过低警告线
	 */
	public static float LIEDU_LOW_WARING = -(float) 5.000;
	public static float MAICHONG_LOW_WARING = -(float) 5.000;
	public static float YUDU_LOW_WARING = -(float) 5.000;
	public static float FENGZHI_LOW_WARING = -(float) 5.000;
	public static float QIAODU_LOW_WARING = -(float) 5.000;
	public static float BOXING_LOW_WARING = - (float) 5.000;
	
	/*************************************************保存容量设置参数*************************************************************/	
	/**
	 * 每秒原始数据文件保存的最大数量（300即保存五分钟的）
	 */
	public final static int PER_1S_HISTORY_FILE_MAX_NUM = 300;
	/**
	 * 每秒无量纲指标数据文件保存的最大数量(300即保存五分钟的 )
	 */
	public final static int PER_1S_DIMENSION_LESS_FILE_MAX_NUM = 300;
	/**
	 * 每5分钟原始数据文件保存的最大数量（8640即最多保存一个月的）
	 */
	public final static int PER_5MIN_HISTORY_FILE_MAX_NUM = 8640;
	/**
	 * 每5分钟无量纲指标数据文件保存的最大数量(8640即最多保存一个月的)
	 */
	public final static int PER_5MIN_DIMENSION_LESS_FILE_MAX_NUM = 8640;
	
	
/*************************************************更新时间设置参数*************************************************************/	
	/**
	 * 无量纲指标默认刷新时间间隔(ms)
	 */
	public static int UPDATE_DIMENSIONLESS_PER_TIME = 1000;
	/**
	 * 实时采集数据默认刷新时间间隔(ms)
	 */
	public static int UPDATE_CUR_DATA_PER_TIME = 1000;
	/**
	 * 更新诊断报告时间间隔(ms)
	 */
	public static int UPDATE_REPORT_PER_TIME = 1000;
	/**
	 * 多久生成一份报告(计满多少个点)
	 */
	public static int CREATE_REPORT_COUNT = 0;
	/**
	 * 满多少个计数点就生成报告（秒）
	 */
	public static int CREATE_REPORT_PER_COUNT = 1;
	/**
	 * 报告显示最大数量
	 */
	public static int REPORTS_MAX = 60 ;
	/**
	 * 秒计数点（用于抽样不同时间段的数据保存到不同的文件夹下）
	 */
	public static int SecondCount = 0;
//	public static int WebSecondCount = 0;
	
	/**
	 * 显示最近一分钟数据
	 */
	public final static int SHOW_CUR_1MIN = 0;
	/**
	 * 显示最近五分钟数据
	 */
	public final static int SHOW_CUR_5MIN = 1;
	/**
	 * 显示自定义时间段数据
	 */
	public final static int SHOW_CUSTOM = 2;
	
}
