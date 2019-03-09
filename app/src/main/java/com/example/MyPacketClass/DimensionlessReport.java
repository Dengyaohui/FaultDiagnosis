package com.example.MyPacketClass;

public class DimensionlessReport {

	/**
	 * 时间（使用文件名）
	 */
	private String DateTime = null;
	/**
	 * 指标数值
	 */
	private float liedu_data = (float) 0.000;
	private float maichong_data = (float) 0.000;
	private float yudu_data = (float) 0.000;
	private float fengzhi_data = (float) 0.000;
	private float qiaodu_data = (float) 0.000;
	private float boxing_data = (float) 0.000;
	/**
	 * 指标状态
	 */
	private String liedu_state = null;
	private String maichong_state = null;
	private String yudu_state = null;
	private String fengzhi_state = null;
	private String qiaodu_state = null;
	private String boxing_state = null;
	
	public DimensionlessReport() {
		// TODO Auto-generated constructor stub
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public float getLiedu_data() {
		return liedu_data;
	}

	public void setLiedu_data(float liedu_data) {
		this.liedu_data = liedu_data;
	}

	public float getMaichong_data() {
		return maichong_data;
	}

	public void setMaichong_data(float maichong_data) {
		this.maichong_data = maichong_data;
	}

	public float getYudu_data() {
		return yudu_data;
	}

	public void setYudu_data(float yudu_data) {
		this.yudu_data = yudu_data;
	}

	public float getFengzhi_data() {
		return fengzhi_data;
	}

	public void setFengzhi_data(float fengzhi_data) {
		this.fengzhi_data = fengzhi_data;
	}

	public float getQiaodu_data() {
		return qiaodu_data;
	}

	public void setQiaodu_data(float qiaodu_data) {
		this.qiaodu_data = qiaodu_data;
	}

	public float getBoxing_data() {
		return boxing_data;
	}

	public void setBoxing_data(float boxing_data) {
		this.boxing_data = boxing_data;
	}

	public String getLiedu_state() {
		return liedu_state;
	}

	public void setLiedu_state(String liedu_state) {
		this.liedu_state = liedu_state;
	}

	public String getMaichong_state() {
		return maichong_state;
	}

	public void setMaichong_state(String maichong_state) {
		this.maichong_state = maichong_state;
	}

	public String getYudu_state() {
		return yudu_state;
	}

	public void setYudu_state(String yudu_state) {
		this.yudu_state = yudu_state;
	}

	public String getFengzhi_state() {
		return fengzhi_state;
	}

	public void setFengzhi_state(String fengzhi_state) {
		this.fengzhi_state = fengzhi_state;
	}

	public String getQiaodu_state() {
		return qiaodu_state;
	}

	public void setQiaodu_state(String qiaodu_state) {
		this.qiaodu_state = qiaodu_state;
	}

	public String getBoxing_state() {
		return boxing_state;
	}

	public void setBoxing_state(String boxing_state) {
		this.boxing_state = boxing_state;
	}

	
}
