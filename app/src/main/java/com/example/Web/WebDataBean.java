package com.example.Web;

import java.util.ArrayList;

/**
 * 貌似暂时不需要这个Bean，原来就有保存数据的方法了
 */

public class WebDataBean {

	private String liedu;
	private String maichong;
	private String yudu;
	private String fengzhi;
	private String qiaodu;
	public static ArrayList<Float> boxing = new ArrayList<>();
	private String wendu;

	public String getWendu() {
		return wendu;
	}

	public void setWendu(String wendu) {
		this.wendu = wendu;
	}

	public String getLiedu() {
		return liedu;
	}

	public void setLiedu(String liedu) {
		this.liedu = liedu;
	}

	public String getMaichong() {
		return maichong;
	}

	public void setMaichong(String maichong) {
		this.maichong = maichong;
	}

	public String getYudu() {
		return yudu;
	}

	public void setYudu(String yudu) {
		this.yudu = yudu;
	}

	public String getFengzhi() {
		return fengzhi;
	}

	public void setFengzhi(String fengzhi) {
		this.fengzhi = fengzhi;
	}

	public String getQiaodu() {
		return qiaodu;
	}

	public void setQiaodu(String qiaodu) {
		this.qiaodu = qiaodu;
	}

	public ArrayList<Float> getBoxing() {
		return boxing;
	}

	public void setBoxing(ArrayList<Float> volet) {
		this.boxing = volet;
//		ClassShowCurData.inBuf = (ArrayList<short[]>) volet.clone();
	}

}
