package com.example.FileUtils;

/**
 * 根据参数计算不同的无量纲指标并返回
 * @author Administrator
 *
 */
public class ClassDealPCMdata{

	/**
	 * 历史数据
	 */
	private Float[] historyData = null;
	/**
	 * 计算的指标类型     0>烈度  1>脉冲  2>欲度 3>峰值  4>峭度 5>波形
	 */
	private int arithmeticType;

	public ClassDealPCMdata(Float[] historyData, int arithmeticType){
		this.historyData = historyData;
		this.arithmeticType = arithmeticType;
	}

	public float getDimensionless() {

		float result=0;

		switch (arithmeticType) {
			case 0:
				result = deal_Dimensionless_Parameter(historyData, 0);
				break;
			case 1:
				result = deal_Dimensionless_Parameter(historyData, 1);
				break;
			case 2:
				result = deal_Dimensionless_Parameter(historyData, 2);
				break;
			case 3:
				result = deal_Dimensionless_Parameter(historyData, 3);
				break;
			case 4:
				result = deal_Dimensionless_Parameter(historyData, 4);
				break;
			case 5:
				result = deal_Dimensionless_Parameter(historyData, 5);
				break;
			default:
				break;
		}

		return result;
	}

	/**
	 * return 无量纲指标参数的值（平均值）
	 * param yData
	 *
	 * @param colleteData
	 * @param arithmeticType     使用的算法种类：0>计算烈度  1>计算脉冲  2>计算欲度  3>计算峰值  4>计算峭度 5>计算波形
	 * @return
	 */
	private float deal_Dimensionless_Parameter(Float[] colleteData , int arithmeticType){
		float average_Value = 0;
		float sum = 0;
		switch (arithmeticType) {
			//in here ,can use different arithmetic
			case 0:
				for (int i = 0; i < colleteData.length; i++) {
					sum = sum+colleteData[i]*2/3/7;
				}
				break;
			case 1:
				for (int i = 0; i < colleteData.length; i++) {
					sum = sum+colleteData[i]*4/5/6;
				}
				break;
			case 2:
				for (int i = 0; i < colleteData.length; i++) {
					sum = sum+colleteData[i]*1/2/7;
				}
				break;
			case 3:
				for (int i = 0; i < colleteData.length; i++) {
					sum = sum+colleteData[i]*3/5/6;
				}
				break;
			case 4:
				for (int i = 0; i < colleteData.length; i++) {
					sum = sum+colleteData[i]*3/2/9;
				}
				break;
			case 5:
				for (int i = 0; i < colleteData.length; i++) {
					sum = sum+colleteData[i]*1/3/7;
				}
				break;
			default:
				break;
		}
		//保留三位小数
		average_Value = (float)(Math.round(sum/colleteData.length*1000)/1000.0);
		return average_Value;
	}
}
